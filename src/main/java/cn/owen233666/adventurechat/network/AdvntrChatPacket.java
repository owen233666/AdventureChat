package cn.owen233666.adventurechat.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AdvntrChatPacket(String name, int age) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AdvntrChatPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("mymod", "ac_data"));
    public static final StreamCodec<ByteBuf, AdvntrChatPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            AdvntrChatPacket::name,
            ByteBufCodecs.VAR_INT,
            AdvntrChatPacket::age,
            AdvntrChatPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
