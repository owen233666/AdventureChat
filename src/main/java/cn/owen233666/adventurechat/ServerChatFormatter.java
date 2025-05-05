package cn.owen233666.adventurechat;

import cn.owen233666.adventurechat.utils.convertutils;
import cn.owen233666.adventurechat.utils.matchBilibiliVideos;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerChatFormatter {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String rawMessage = matchBilibiliVideos.bilibilimatcher(convertutils.convert(event.getRawText()));
        Component PlayerHover;
        try{
            PlayerHover = convertToMinecraft(
                    AdventureChat.MINI_MESSAGE.deserialize("<hover:show_text:'发送时间:"+ DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) +"'>"+player.getScoreboardName()+"<reset>"),
                    player.registryAccess()
            );
        }catch (Exception e){
            PlayerHover = Component.literal("解析文字时发生错误！")
                    .withStyle(ChatFormatting.RED);
        }

        // 2. 解析消息内容
        Component messageContent;
        try {
            messageContent = convertToMinecraft(
                    AdventureChat.MINI_MESSAGE.deserialize(rawMessage),
                    player.registryAccess()
            );
        } catch (Exception e) {
            messageContent = Component.literal("解析文字时发生错误！")
                    .withStyle(ChatFormatting.RED);
        }

        // 3. 组合成最终消息
        Component finalMessage = Component.empty()
                .append(PlayerHover)
                .append(": ")
                .append(messageContent);

        // 4. 取消原版消息并发送自定义消息
        event.setCanceled(true);
        player.server.getPlayerList().broadcastSystemMessage(finalMessage, false);
    }

    public static Component convertToMinecraft(net.kyori.adventure.text.Component component, HolderLookup.Provider registries) {
        try {
            String json = GsonComponentSerializer.gson().serialize(component);
            return Component.Serializer.fromJson(json, registries);
        } catch (Exception e) {
            return Component.literal("[解析错误]").withStyle(ChatFormatting.RED);
        }
    }
}
