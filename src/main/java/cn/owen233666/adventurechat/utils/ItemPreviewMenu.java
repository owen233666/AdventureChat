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

import java.awt.*;

public class ItemPreviewMenu extends AbstractContainerMenu {
    private final ItemStack displayItem;

    public ItemPreviewMenu(int id, Inventory playerInv, Component Title, ItemStack displayItem) {
        super(MenuType.GENERIC_9x3, id);
        this.displayItem = displayItem.copy();

        this.addSlot(new DisplayOnlySlot(displayItem, 0, 80, 36));

        // 玩家背包
        addPlayerInventory(playerInv);
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