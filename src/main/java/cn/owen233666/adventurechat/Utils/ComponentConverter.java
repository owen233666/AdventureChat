package cn.owen233666.adventurechat.Utils;

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;

public class ComponentConverter {
    public static net.minecraft.network.chat.Component convertToMinecraft(net.kyori.adventure.text.Component component, HolderLookup.Provider registries) {
        try {
            String json = GsonComponentSerializer.gson().serialize(component);
            return net.minecraft.network.chat.Component.Serializer.fromJson(json, registries);
        } catch (Exception e) {
            return net.minecraft.network.chat.Component.literal("解析错误").withStyle(ChatFormatting.RED);
        }
    }
}
