package cn.owen233666.adventurechat.commands;

import cn.owen233666.adventurechat.AdventureChat;
import cn.owen233666.adventurechat.Config;
import cn.owen233666.adventurechat.client.ToggleButton;
import cn.owen233666.adventurechat.utils.ItemData;
import cn.owen233666.adventurechat.utils.ItemShowCache;
import com.google.common.cache.Cache;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.UUID;
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
                        .then(Commands.argument("data", StringArgumentType.greedyString())
                                .executes(context -> {
                                    try {
                                        return execute(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "data")
                                        );
                                    } catch (ExecutionException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                        )
                );
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
            Component message = Component.Serializer.fromJson(json, source.registryAccess()).append(String.valueOf(ToggleButton.isButtonOn));

            source.sendSuccess(() -> message, false);
        } catch (Exception e) {
            source.sendSuccess(
                    () -> Component.literal("消息解析错误: " + e.getMessage()).withStyle(ChatFormatting.RED), false);
        }
    }

    private static int execute(CommandSourceStack source, String data) throws ExecutionException {
        if (source.isPlayer() && !(source.getPlayer() instanceof FakePlayer)) {
            // 在这里处理你的命令逻辑
            // data参数包含用户输入的所有内容
            ServerPlayer player = source.getPlayer();
            ItemData itemData = ItemShowCache.cache.get(UUID.fromString(data));
            if(itemData == null){
                return Command.SINGLE_SUCCESS;
            }
            Player itemOwner = itemData.getPlayer();
            ItemStack itemStack = itemData.getItem();
            SimpleContainer container = new SimpleContainer(27);
            container.addItem(itemStack.copy());
            AbstractContainerMenu ItemPreviewMenu = new ChestMenu(MenuType.GENERIC_9x3, 0, player.getInventory(), container, 3){
                @Override
                public boolean stillValid(Player player){
                    return true;
                }
                @Override
                public ItemStack quickMoveStack(Player player, int index){
                    return ItemStack.EMPTY;
                }
            };
            player.openMenu((MenuProvider) ItemPreviewMenu);
            return Command.SINGLE_SUCCESS;
        }

        source.sendFailure(Component.literal("只有真实玩家才能执行此命令"));
        return 0;
    }

}