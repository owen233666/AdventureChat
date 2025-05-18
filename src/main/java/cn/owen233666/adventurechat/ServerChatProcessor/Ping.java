package cn.owen233666.adventurechat.ServerChatProcessor;

import cn.owen233666.adventurechat.Utils.ComponentConverter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ping {
    private static MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

    public static String processPingRequest(ServerPlayer sender, String message) {
        List<ServerPlayer> serverPlayers = server.getPlayerList().getPlayers();
        for (ServerPlayer player : serverPlayers) {
            Pattern p = Pattern.compile(player.getScoreboardName());
            Matcher m = p.matcher(message);
            if (m.find()) {
                if (sender == player) {
                    return message;
                }
                String mm = "<green>" + player.getScoreboardName() + "<lang:message.chat.ping>" + "<reset>";
                net.kyori.adventure.text.Component miniMessage = MiniMessage.miniMessage().deserialize(mm);
                net.minecraft.network.chat.Component finalComponent = ComponentConverter.convertToMinecraft(miniMessage, player.registryAccess());
                // 发送给被ping的玩家
                player.displayClientMessage(finalComponent, true);
                // 返回MiniMessage格式的字符串用于聊天
                return p.matcher(message).replaceAll("<#CC6600>@<reset><#FF7F00>" + player.getScoreboardName() + "<reset>");
            }
        }
        return message;
    }
}