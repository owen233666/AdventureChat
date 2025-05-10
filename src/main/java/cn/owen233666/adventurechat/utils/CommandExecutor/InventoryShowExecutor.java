package cn.owen233666.adventurechat.utils.CommandExecutor;

import cn.owen233666.adventurechat.utils.Cache.InventoryShowCache;
import cn.owen233666.adventurechat.utils.DataType.InventoryData;
import cn.owen233666.adventurechat.utils.LockedSlot;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import net.minecraft.commands.CommandSourceStack;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class InventoryShowExecutor {
    public static int execute(CommandSourceStack source, String uuid) throws ExecutionException{
        if(source.isPlayer() && !(source.getPlayer() instanceof FakePlayer)){
            ServerPlayer player = source.getPlayer();
            InventoryData inventoryData = InventoryShowCache.cache.get(UUID.fromString(uuid));
            Inventory inventory = inventoryData.getInventory();

            player.openMenu(new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return Component.empty()
                            .append(inventoryData.getPlayer().getScoreboardName())
                            .append(Component.translatable("title.playersinv"));
                }

                @Override
                public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                    SimpleContainer container = new SimpleContainer(45);
                    ItemStack glassPane = new ItemStack(Items.WHITE_STAINED_GLASS_PANE);
                    glassPane.remove(DataComponents.CUSTOM_NAME); // 移除自定义名称
                    glassPane.remove(DataComponents.ITEM_NAME);  // 移除物品默认名称（如有）
                    glassPane.remove(DataComponents.LORE);         // 移除 Lore 文本
                    glassPane.remove(DataComponents.HIDE_TOOLTIP); // 确保不禁用 Tooltip（反向操作）
                    glassPane.set(DataComponents.CUSTOM_NAME, Component.empty()); // 设置自定义名称为空

                    ItemStack playerHead = new ItemStack(Items.PLAYER_HEAD);
                    GameProfile profile = new GameProfile(inventoryData.getPlayer().getUUID(), inventoryData.getPlayer().getScoreboardName());
                    ResolvableProfile resolvableProfile = new ResolvableProfile(profile);
                    playerHead.set(DataComponents.PROFILE, resolvableProfile);
                    playerHead.set(DataComponents.CUSTOM_NAME, Component.literal(inventoryData.getPlayer().getScoreboardName())); // 设置自定义名称为空
                    for (int i = 0; i < 45; i++) {
                        if(i<=35){
                            container.setItem(i, inventory.getItem(i).copy());
                        }
                        if(i == 36){
                            container.setItem(i, glassPane.copy());
                        }
                        if(i >= 37 && i <= 40){
                            container.setItem(i, inventory.getArmor(i-1).copy());
                        }
                        if(i == 41 || i == 42){
                            container.setItem(i, glassPane.copy());
                        }
                        if(i == 43){
                            container.setItem(i, inventory.getItem(40).copy());
                        }
                        if(i == 44){
                            container.setItem(i, playerHead.copy());
                        }
                    }

                    return new ChestMenu(MenuType.GENERIC_9x5, containerId, playerInventory, container, 5) {
                        @Override
                        public @NotNull Slot addSlot(@NotNull Slot slot) {
                            // 替换所有容器槽为 LockedSlot（禁止交互）
                            if (slot.container != playerInventory) {
                                return super.addSlot(new LockedSlot(slot.container, slot.getSlotIndex(), slot.x, slot.y));
                            }
                            return super.addSlot(slot); // 玩家背包槽保持正常
                        }

                        @Override
                        public ItemStack quickMoveStack(@NotNull Player player, int index) {
                            return ItemStack.EMPTY;
                        }
                    };
                }
            });

            return Command.SINGLE_SUCCESS;
        }

        source.sendFailure(Component.translatable("command.fail.onlyplayer"));
        return 0;
    }
}
