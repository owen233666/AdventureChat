package cn.owen233666.adventurechat.network;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Networking {
    private static final String VERSION = "1";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(VERSION);
        registrar.playToServer(AdventureTogglePacket.TYPE, AdventureTogglePacket.STREAM_CODEC, AdventureTogglePacket::handleOnServer);
    }

    public static void sendToServer(AdventureTogglePacket packet) {
        Minecraft.getInstance().getConnection().send(packet);
    }
}
