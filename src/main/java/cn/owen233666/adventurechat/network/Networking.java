package cn.owen233666.adventurechat.network;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Networking {
    private static final String VERSION = "1";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar registrar = event.registrar(VERSION);

//        registrar.playToClient(ClientAdvntrChatPacket.TYPE, ClientAdvntrChatPacket.STREAM_CODEC, ClientAdvntrChatPacket::handleOnClient);
        registrar.playToServer(ClientAdvntrChatPacket.TYPE, ClientAdvntrChatPacket.STREAM_CODEC, ClientAdvntrChatPacket::handleOnServer);
    }


    public static void sendToServer(ClientAdvntrChatPacket packet) {
        Minecraft.getInstance().getConnection().send(packet);
    }
}
