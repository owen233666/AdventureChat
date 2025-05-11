package cn.owen233666.adventurechat.network;

import cn.owen233666.adventurechat.AdventureChat;
import cn.owen233666.adventurechat.ServerChatProcessor.ServerChatFormatter;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record AdventureTogglePacket(Boolean isAdvntrapiOn) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AdventureTogglePacket> TYPE = new CustomPacketPayload.Type<>(AdventureChat.id("adventurechat_data"));
    public static final StreamCodec<ByteBuf, AdventureTogglePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            AdventureTogglePacket::isAdvntrapiOn,
            AdventureTogglePacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
//    UNUSED
//    public static void handleOnClient(final ClientAdvntrChatPacket data, final IPayloadContext context) {
//
//    }

    public static void handleOnServer(final AdventureTogglePacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            // 更新 isAdvntrAPIOn 的值
            ServerChatFormatter.isAdvntrAPIOn = data.isAdvntrapiOn();

        });
    }
}
