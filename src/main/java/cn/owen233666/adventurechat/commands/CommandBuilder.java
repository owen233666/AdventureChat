package cn.owen233666.adventurechat.commands;

import cn.owen233666.adventurechat.AdventureChat;
import cn.owen233666.adventurechat.Config;
import cn.owen233666.adventurechat.client.ToggleButton;
import cn.owen233666.adventurechat.utils.ItemData;
import cn.owen233666.adventurechat.utils.ItemShowCache;
import cn.owen233666.adventurechat.utils.LockedSlot;
import com.google.common.cache.Cache;
import com.mojang.authlib.GameProfile;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.Optional;
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
            ServerPlayer player = source.getPlayer();
            ItemData itemData = ItemShowCache.cache.get(UUID.fromString(data));
            if (itemData == null) {
                return Command.SINGLE_SUCCESS;
            }
            ItemStack itemStack = itemData.getItem();

            player.openMenu(new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.empty()
                            .append(itemData.getPlayer().getDisplayName())
                            .append("的物品");
                }

                @Override
                public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                    SimpleContainer container = new SimpleContainer(27);
                    // 创建白色玻璃板
                    ItemStack glassPane = new ItemStack(Items.WHITE_STAINED_GLASS_PANE);
                    glassPane.remove(DataComponents.CUSTOM_NAME); // 移除自定义名称
                    glassPane.remove(DataComponents.ITEM_NAME);  // 移除物品默认名称（如有）
                    glassPane.remove(DataComponents.LORE);         // 移除 Lore 文本
                    glassPane.remove(DataComponents.HIDE_TOOLTIP); // 确保不禁用 Tooltip（反向操作）
                    glassPane.set(DataComponents.CUSTOM_NAME, Component.empty()); // 设置自定义名称为空


                    ItemStack playerHead = new ItemStack(Items.PLAYER_HEAD);
                    GameProfile profile = new GameProfile(itemData.getPlayer().getUUID(), itemData.getPlayer().getScoreboardName());
                    ResolvableProfile resolvableProfile = new ResolvableProfile(profile);
                    playerHead.set(DataComponents.PROFILE, resolvableProfile);

                    for (int i = 0; i < 27 ; i++) {
                        if(i == 0) {
                            container.setItem(0, playerHead.copy());
                        }
                        else if (i == 13 ) {
                            container.setItem(13, itemStack.copy());
                        }
                        else {
                            container.setItem(i, glassPane.copy());
                        }
                    }

                    return new ChestMenu(MenuType.GENERIC_9x3, containerId, playerInventory, container, 3) {
                        @Override
                        protected Slot addSlot(Slot slot) {
                            // 替换所有容器槽为 LockedSlot（禁止交互）
                            if (slot.container != playerInventory) {
                                return super.addSlot(new LockedSlot(slot.container, slot.getSlotIndex(), slot.x, slot.y));
                            }
                            return super.addSlot(slot); // 玩家背包槽保持正常
                        }

                        @Override
                        public ItemStack quickMoveStack(Player player, int index) {
                            return ItemStack.EMPTY; // 禁止 Shift+点击 快速移动
                        }
                    };
                }
            });

            return Command.SINGLE_SUCCESS;
        }

        source.sendFailure(Component.literal("只有真实玩家才能执行此命令"));
        return 0;
    }

}