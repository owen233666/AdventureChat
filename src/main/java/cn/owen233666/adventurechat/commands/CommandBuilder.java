package cn.owen233666.adventurechat.commands;

import cn.owen233666.adventurechat.AdventureChat;
import cn.owen233666.adventurechat.Config;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CommandBuilder {

    public static LiteralArgumentBuilder<CommandSourceStack> buildAModCommand() {
        return Commands.literal("adventurechat")
                .executes(context -> {
                    // 当只输入/adventurechat时，显示用法提示
                    sendFormattedMessage(context.getSource(), "<yellow>用法: /adventurechat help</yellow>");
                    return 1;
                })
                .then(Commands.literal("help")
                        .executes(context -> {
                            // 当输入/adventurechat help时，显示帮助信息
                            sendHelpMessage(context.getSource());
                            return 1;
                        }));
    }

    // 发送帮助信息的辅助方法
    private static void sendHelpMessage(CommandSourceStack source) {
        String helpMessage = Config.HELP_STRING.get();
        sendFormattedMessage(source, helpMessage);
    }

    // 发送格式化消息的通用方法
    private static void sendFormattedMessage(CommandSourceStack source, String miniMessage) {
        try {
            net.kyori.adventure.text.Component adventureComponent = AdventureChat.MINI_MESSAGE.deserialize(miniMessage);
            String json = GsonComponentSerializer.gson().serialize(adventureComponent);
            Component message = net.minecraft.network.chat.Component.Serializer.fromJson(json, source.registryAccess());

            source.sendSuccess(() -> message, false);
        } catch (Exception e) {
            source.sendSuccess(
                    () -> Component.literal("消息解析错误: " + e.getMessage()).withStyle(ChatFormatting.RED), false);
        }
    }
}