package cn.owen233666.adventurechat.utils.DataType;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class InventoryData {
    private Player player;
    private Inventory inventory;

    public void setInventory(Player player, Inventory inventory) {
        this.player = player;
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }
}
