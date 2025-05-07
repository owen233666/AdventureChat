package cn.owen233666.adventurechat.utils;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class ItemPreviewMenu extends AbstractContainerMenu {
    private final ItemStack displayItem;

    public ItemPreviewMenu(int id, Inventory playerInv, ItemStack displayItem) {
        super(MenuType.GENERIC_9x3, id);
        this.displayItem = displayItem.copy();

        // 如果是潜影盒，显示内容
        if (displayItem.getItem() instanceof BlockItem blockItem &&
                blockItem.getBlock() instanceof ShulkerBoxBlock) {

            // 创建3x3容器显示潜影盒内容
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    int slot = j + i * 3;
                    ItemStack content = getShulkerContent(displayItem, slot);
                    this.addSlot(new DisplayOnlySlot(content, slot, 62 + j * 18, 18 + i * 18));
                }
            }
        } else {
            // 普通物品居中显示
            this.addSlot(new DisplayOnlySlot(displayItem, 0, 80, 36));
        }

        // 玩家背包
        addPlayerInventory(playerInv);
    }

    private ItemStack getShulkerContent(ItemStack shulkerBox, int slot) {
        // 获取潜影盒内容实现
        // 这里需要根据NMS实现获取潜影盒内容
        return ItemStack.EMPTY; // 实际实现应返回对应槽位的物品
    }

    private void addPlayerInventory(Inventory playerInv) {
        // 背包主体 (27格)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // 快捷栏 (9格)
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY; // 禁止物品移动
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    // 仅用于显示的Slot
    private static class DisplayOnlySlot extends Slot {
        public DisplayOnlySlot(ItemStack stack, int index, int x, int y) {
            super(new SimpleContainer(stack), index, x, y);
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}