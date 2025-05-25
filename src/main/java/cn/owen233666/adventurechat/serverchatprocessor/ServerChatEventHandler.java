package cn.owen233666.adventurechat.serverchatprocessor;

import cn.owen233666.adventurechat.AdventureChat;
import cn.owen233666.adventurechat.utils.ComponentConverter;
import cn.owen233666.adventurechat.utils.convertutils;
import cn.owen233666.adventurechat.utils.matchBilibiliVideos;
import cn.owen233666.adventurechat.utils.serverAudienceInit;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerChatEventHandler {
    public static boolean isAdvntrAPIOn = true;
    private static Audience AllThePlayers = serverAudienceInit.AudienceInit.adventure().all();
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String rawMessage = event.getRawText();
        Component PlayerHover = Component.empty()
                .append(Component.text(event.getUsername())
                        .hoverEvent(Component.translatable("hover.text.sendtime")
                                .append(Component.text(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())))
                        )
                )
                .append(Component.text(": "));

        if(isAdvntrAPIOn){
//            Component tempMessage = matchBilibiliVideos.bilibilimatcher(convertutils.convertComponent(rawMessage));
            String tempMessage = matchBilibiliVideos.bilibilimatcher(convertutils.convert(rawMessage));
//            String tempMessage = matchBilibiliVideos.bilibilimatcher(convertutils.convert(rawMessage));
//            tempMessage = itemShowProcessor.processItemShow(player, tempMessage);
//            tempMessage = inventoryShowProcessor.processInvtoryShow(player, tempMessage);
//            tempMessage = enderChestShowProcessor.processEnderChestShow(player, tempMessage);
//            tempMessage = Ping.processPingRequest(player, tempMessage);
            MiniMessage mm = MiniMessage.miniMessage();
            Component minimessage = mm.deserialize(tempMessage);
            Component prased =
            event.setCanceled(true);
            AllThePlayers.sendMessage(Component.empty()
                    .append(PlayerHover)
                    .append(prased)
            );
        }else{
            //关闭AdventureAPI解析时候的消息组成
            event.setCanceled(true);
            AllThePlayers.sendMessage(Component.empty()
                    .append(PlayerHover)
                    .append(Component.text(rawMessage)));
        }
    }
}