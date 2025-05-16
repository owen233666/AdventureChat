package cn.owen233666.adventurechat.ServerChatProcessor;

import cn.owen233666.adventurechat.AdventureChat;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ping {
    private static MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

    public static String processPingRequest(ServerPlayer sender, String message){

        List<ServerPlayer> serverPlayers = server.getPlayerList().getPlayers();

        for (ServerPlayer player : serverPlayers) {
            Pattern p = Pattern.compile(player.getScoreboardName());
            Matcher m = p.matcher(message);
            if(m.find()){
                if(sender == player){
                    return message;
                }
//                net.kyori.adventure.text.Component translated = net.kyori.adventure.text.Component.translatable("message.chat.ping");
                net.kyori.adventure.text.Component translated = MiniMessage.miniMessage().deserialize("<green>" + player.getScoreboardName() + Component.translatable("message.chat.ping"));
                player.displayClientMessage(
                        convertToMinecraft(
                                AdventureChat.MINI_MESSAGE
                                        .deserialize(PlainTextComponentSerializer
                                                .plainText()
                                                .serialize(translated)
                                        ),
                                player.registryAccess()),
                        true
                );
                return p.matcher(message).replaceAll("<#CC6600>@<reset><#FF7F00>" + player.getScoreboardName() + "<reset>");
            }
        }
        return message;
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
