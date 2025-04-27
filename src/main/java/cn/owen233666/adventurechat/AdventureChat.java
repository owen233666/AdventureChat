package cn.owen233666.adventurechat;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.minecraft.ChatFormatting;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

@Mod(AdventureChat.MODID)
public class AdventureChat {
    public static final String MODID = "adventurechat";
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public AdventureChat(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String rawMessage = event.getRawText();

        // 使用MiniMessage解析原始消息
        net.kyori.adventure.text.Component adventureComponent = MINI_MESSAGE.deserialize(rawMessage);

        // 转换为Minecraft的Component（修正后的转换方式）
        Component messageComponent = convertAdventureToMinecraft(adventureComponent, player.registryAccess());

        // 构建最终消息
        MutableComponent finalMessage = Component.literal("")
//                .append(player.getDisplayName())
//                .append(": ")
                .append(messageComponent);

        event.setMessage(finalMessage);
    }

    private Component convertAdventureToMinecraft(net.kyori.adventure.text.Component adventureComponent, HolderLookup.Provider registries) {
        try {
            String json = GsonComponentSerializer.gson().serialize(adventureComponent);
            // 使用带registries参数的fromJson方法
            MutableComponent component = Component.Serializer.fromJson(json, registries);
            return component != null ? component : Component.literal("(解析错误)");
        } catch (Exception e) {
            return Component.literal("(消息解析失败)").withStyle(ChatFormatting.RED);
        }
    }
}