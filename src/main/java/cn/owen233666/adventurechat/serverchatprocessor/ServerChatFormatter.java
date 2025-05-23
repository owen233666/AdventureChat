package cn.owen233666.adventurechat.serverchatprocessor;

import cn.owen233666.adventurechat.AdventureChat;
import cn.owen233666.adventurechat.utils.ComponentConverter;
import cn.owen233666.adventurechat.utils.convertutils;
import cn.owen233666.adventurechat.utils.matchBilibiliVideos;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerChatFormatter {
    public static boolean isAdvntrAPIOn = true;
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String rawMessage = event.getRawText();
        Component PlayerHover;

        try{
            PlayerHover = ComponentConverter.convertToMinecraft(
                    AdventureChat.MINI_MESSAGE.deserialize("<hover:show_text:'<lang:hover.text.sendtime>"+ DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) +"'>"+player.getScoreboardName()+"<reset>"),
                    player.registryAccess()
            );
        }catch (Exception e){
            PlayerHover = Component.translatable("message.adventureapi.failedtoanalyze")
                    .withStyle(ChatFormatting.RED);
        }



        if(isAdvntrAPIOn){
            String tempMessage = matchBilibiliVideos.bilibilimatcher(convertutils.convert(rawMessage));
            tempMessage = itemShowProcessor.processItemShow(player, tempMessage);
            tempMessage = processInventoryShow.processInvtoryShow(player, tempMessage);
            tempMessage = processEnderChestShow.processEnderChestShow(player, tempMessage);
            tempMessage = Ping.processPingRequest(player, tempMessage);
            // 2. 解析消息内容
//            Component messageContent = Component.literal(LegacyComponentSerializer.legacyAmpersand().deserialize(tempMessage).toString());
            MiniMessage mm = MiniMessage.miniMessage();
            net.kyori.adventure.text.Component prased = mm.deserialize(tempMessage);
            // 3. 组合成最终消息
            Component finalMessage = Component.empty().append(PlayerHover).append(": ").append(ComponentConverter.convertToMinecraft(prased,player.registryAccess()));
            // 4. 取消原版消息并发送自定义消息
            event.setCanceled(true);
            player.server.getPlayerList().broadcastSystemMessage(finalMessage, false);
        }else{
            Component playerMessage = Component.empty().append(PlayerHover).append(": ").append(rawMessage); //关闭AdventureAPI解析时候的消息组成
            event.setCanceled(true);
            player.server.getPlayerList().broadcastSystemMessage(playerMessage, false);
        }
    }
}