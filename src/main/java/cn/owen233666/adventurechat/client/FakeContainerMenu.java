package cn.owen233666.adventurechat.client;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FakeContainerMenu extends AbstractContainerMenu {
    private final Container container;

    public FakeContainerMenu(int containerId, Inventory playerInventory, ItemStack displayItem) {
        super(MenuType.GENERIC_9x3, containerId); // 小型箱子（9x3=27格）

        // 创建一个虚拟容器（27格）
        this.container = new SimpleContainer(27) {
            @Override
            public boolean stillValid(Player player) {
                return true; // 始终允许查看
            }

            @Override
            public ItemStack removeItem(int slot, int amount) {
                return ItemStack.EMPTY; // 禁止拿走物品
            }

            @Override
            public ItemStack getItem(int slot) {
                if (slot == 13) { // 正中间（第13格）
                    return displayItem.copy(); // 返回玩家手持物品的副本
                }
                return ItemStack.EMPTY; // 其他格子为空
            }
        };

        // 添加玩家背包（可选）
        addPlayerInventorySlots(playerInventory);
    }

    @Override
    public boolean stillValid(Player player) {
        return true; // 始终允许查看
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY; // 禁止快速移动物品
    }

    // 添加玩家背包（可选）
    private void addPlayerInventorySlots(Inventory playerInventory) {
        // 玩家背包（27格）
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // 玩家快捷栏（9格）
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }
}
