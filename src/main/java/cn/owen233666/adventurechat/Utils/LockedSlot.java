package cn.owen233666.adventurechat.Utils;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class LockedSlot extends Slot {
    public LockedSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPickup(Player player) {
        return false; // 禁止玩家拿取物品
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false; // 禁止玩家放入物品
    }
}
