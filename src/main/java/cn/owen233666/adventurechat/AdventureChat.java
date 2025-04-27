package cn.owen233666.adventurechat;

import cn.owen233666.adventurechat.title.TitleSystem;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.ChatFormatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

@Mod(AdventureChat.MODID)
public class AdventureChat {
    public static final String MODID = "adventurechat";
    private static final Logger LOGGER = LoggerFactory.getLogger(AdventureChat.class);
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public AdventureChat(IEventBus modEventBus) {
        // 初始化数据库和事件监听
        try {
            Files.createDirectories(Paths.get("./config/adventurechat"));
            TitleSystem.initDatabase("./config/adventurechat/titles.db");
//            TitleSystem.startAutoSave();
        } catch (Exception e) {
            LOGGER.error("初始化失败", e);
        }

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String rawMessage = event.getRawText();

        try {
            // 1. 构建带称号的玩家名
            Component playerName = TitleSystem.getTitledName(player);

            // 2. 解析消息内容
            Component messageContent = parseMessageContent(rawMessage, player);

            // 3. 组合最终消息
            Component finalMessage = Component.empty()
                    .append(playerName)
                    .append(Component.text(": ").withStyle(ChatFormatting.GRAY))
                    .append(messageContent);

            // 4. 取消原版消息并发送自定义格式
            event.setCanceled(true);
            player.server.getPlayerList().broadcastSystemMessage(finalMessage, false);

        } catch (Exception e) {
            LOGGER.warn("处理聊天消息失败", e);
            // 回退到原始消息
            event.setCanceled(false);
        }
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        TitleCommand.register(event.getDispatcher());
    }

    private Component parseMessageContent(String raw, ServerPlayer player) {
        try {
            return convertToMinecraft(
                    MINI_MESSAGE.deserialize(raw),
                    player.registryAccess()
            );
        } catch (Exception e) {
            return Component.literal(raw)
                    .withStyle(ChatFormatting.RED)
                    .append(Component.literal(" (解析失败)")
                            .withStyle(ChatFormatting.DARK_RED));
        }
    }

    private Component convertToMinecraft(net.kyori.adventure.text.Component component,
                                         HolderLookup.Provider registries) {
        try {
            String json = GsonComponentSerializer.gson().serialize(component);
            return Component.Serializer.fromJson(json, registries);
        } catch (Exception e) {
            return Component.literal("[格式错误]")
                    .withStyle(ChatFormatting.RED);
        }
    }
}