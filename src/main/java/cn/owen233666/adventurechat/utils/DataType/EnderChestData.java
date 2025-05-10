package cn.owen233666.adventurechat.utils.DataType;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class EnderChestData {
    private Player player;
    private Inventory enderChest;
    public EnderChestData setEnderChest(Player player, Inventory enderChest) {
        this.player = player;
        this.enderChest = enderChest;
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getEnderChest() {
        return enderChest;
    }
}
