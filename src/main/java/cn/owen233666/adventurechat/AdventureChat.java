package cn.owen233666.adventurechat;

import cn.owen233666.adventurechat.utils.convertHexColorCodes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.ChatFormatting;

@Mod(AdventureChat.MODID)
public class AdventureChat {
    public static final String MODID = "adventurechat";
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public AdventureChat(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String rawMessage = convertHexColorCodes.convertColorCodes(event.getRawText());

        // 1. 构建自定义玩家名显示（无<Dev>前缀）
        MutableComponent playerName = Component.literal(player.getScoreboardName());

        // 2. 解析消息内容
        Component messageContent;
        try {
            messageContent = convertToMinecraft(
                    MINI_MESSAGE.deserialize(rawMessage),
                    player.registryAccess()
            );
        } catch (Exception e) {
            messageContent = Component.literal(rawMessage)
                    .withStyle(ChatFormatting.RED);
        }

        // 3. 组合成最终消息
        Component finalMessage = Component.empty()
                .append(playerName)
                .append(": ")
                .append(messageContent);

        // 4. 取消原版消息并发送自定义消息
        event.setCanceled(true);
        player.server.getPlayerList().broadcastSystemMessage(finalMessage, false);
    }

    private Component convertToMinecraft(net.kyori.adventure.text.Component component, HolderLookup.Provider registries) {
        try {
            String json = GsonComponentSerializer.gson().serialize(component);
            return Component.Serializer.fromJson(json, registries);
        } catch (Exception e) {
            return Component.literal("[解析错误]").withStyle(ChatFormatting.RED);
        }
    }
}