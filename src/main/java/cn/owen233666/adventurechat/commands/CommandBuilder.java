package cn.owen233666.adventurechat.commands;

import cn.owen233666.adventurechat.AdventureChat;
import cn.owen233666.adventurechat.Config;
import cn.owen233666.adventurechat.client.ToggleButton;
import cn.owen233666.adventurechat.utils.CommandExecutor.InventoryShowExecutor;
import cn.owen233666.adventurechat.utils.CommandExecutor.ItemShowExecutor;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.concurrent.ExecutionException;

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
                        }))
                .then(Commands.literal("previewitem")
                        .then(Commands.argument("uuid", StringArgumentType.greedyString())
                                .executes(context -> {
                                    try {
                                        return ItemShowExecutor.execute(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "uuid")
                                        );
                                    } catch (ExecutionException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                        )
                )
                .then(Commands.literal("previewinventory")
                        .then(Commands.argument("uuid", StringArgumentType.greedyString())
                                .executes(context -> {
                                    try {
                                        return InventoryShowExecutor.execute(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "uuid")
                                        );
                                    } catch (ExecutionException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                        )
                );
//                .then(Commands.literal("previewenderchest")
//                        .then(Commands.argument("uuid", StringArgumentType.greedyString())
//                                .executes(context -> {
//                                    try {
//                                        return InventoryShowExecutor.execute(
//                                                context.getSource(),
//                                                StringArgumentType.getString(context, "uuid")
//                                                );
//                                        } catch (ExecutionException e) {
//                                            throw new RuntimeException(e);
//                                    }
//                                })
//                        )
//                );
    }
    private static void sendHelpMessage(CommandSourceStack source) {
        String helpMessage = Config.HELP_STRING.get();
        sendFormattedMessage(source, helpMessage);
    }
    private static void sendFormattedMessage(CommandSourceStack source, String miniMessage) {
        try {
            net.kyori.adventure.text.Component adventureComponent = AdventureChat.MINI_MESSAGE.deserialize(miniMessage);
            String json = GsonComponentSerializer.gson().serialize(adventureComponent);
            Component message = Component.Serializer.fromJson(json, source.registryAccess()).append(String.valueOf(ToggleButton.isButtonOn));

            source.sendSuccess(() -> message, false);
        } catch (Exception e) {
            source.sendSuccess(
                    () -> Component.literal("消息解析错误: " + e.getMessage()).withStyle(ChatFormatting.RED), false);
        }
    }

}