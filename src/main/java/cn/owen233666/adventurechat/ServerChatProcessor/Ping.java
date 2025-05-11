package cn.owen233666.adventurechat.ServerChatProcessor;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ping {
    public ServerPlayer sender;
    public ServerPlayer reciever;
    private MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    public String processPingRequest(ServerPlayer sender, String message){
        List<ServerPlayer> serverPlayers = server.getPlayerList().getPlayers();
        for (ServerPlayer player : serverPlayers) {
            Pattern p = Pattern.compile(player.getScoreboardName());
            Matcher m = p.matcher(message);
            if(message.contains(player.getScoreboardName())){

            }
        }
    }
}
