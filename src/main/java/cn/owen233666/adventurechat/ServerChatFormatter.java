package cn.owen233666.adventurechat;

import cn.owen233666.adventurechat.utils.*;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ServerChatFormatter {
    public static boolean isAdvntrAPIOn = true;
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String rawMessage = event.getRawText();
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
        if(isAdvntrAPIOn){
            String tempMessage = matchBilibiliVideos.bilibilimatcher(convertutils.convert(rawMessage));
            tempMessage = processItemShow(player, tempMessage);
            // 2. 解析消息内容
            Component messageContent;
            try {
                messageContent = convertToMinecraft(
                        AdventureChat.MINI_MESSAGE.deserialize(tempMessage),
                        player.registryAccess()
                );
            } catch (Exception e) {
                messageContent = Component.literal("解析文字时发生错误！")
                        .withStyle(ChatFormatting.RED);
            }
            // 3. 组合成最终消息
            Component finalMessage = Component.empty().append(PlayerHover).append(": ").append(messageContent);
            // 4. 取消原版消息并发送自定义消息
            event.setCanceled(true);
            player.server.getPlayerList().broadcastSystemMessage(finalMessage, false);
        }else{
            Component playerMessage = Component.empty().append(PlayerHover).append(": ").append(rawMessage); //关闭AdventureAPI解析时候的消息组成
            event.setCanceled(true);
            player.server.getPlayerList().broadcastSystemMessage(playerMessage, false);
        }
    }
    private static String processItemShow(ServerPlayer player, String message) {
        if (!message.contains("%i")) return message;
        UUID uuid = UUID.randomUUID();
        ItemData itemData = new ItemData().setItem(player.getMainHandItem()).setPlayer(player);
        ItemStack heldItem = player.getMainHandItem();
        //写入缓存
        ItemShowCache.cache.put(uuid, itemData);
//
//        if (heldItem.isEmpty()) {
//            return message.replace("%i", "[空手]");
//        }

        String itemDisplay = "[<click:run_command:'/adventurechat previewitem "+ uuid +"'>" +heldItem.getItem().getDefaultInstance().getDisplayName()+ "<reset>]";

        return message.replace("%i", itemDisplay);
    }

    public static Component convertToMinecraft(net.kyori.adventure.text.Component component, HolderLookup.Provider registries) {
        try {
            String json = GsonComponentSerializer.gson().serialize(component);
            return Component.Serializer.fromJson(json, registries);
        } catch (Exception e) {
            return Component.literal("解析错误").withStyle(ChatFormatting.RED);
        }
    }
}