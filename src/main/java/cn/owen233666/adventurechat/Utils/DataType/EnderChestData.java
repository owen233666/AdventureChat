package cn.owen233666.adventurechat.Utils.DataType;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;

public class EnderChestData {
    private Player player;
    private PlayerEnderChestContainer enderChest;
    public EnderChestData setEnderChest(Player player, PlayerEnderChestContainer enderChest) {
        this.player = player;
        this.enderChest = enderChest;
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerEnderChestContainer getEnderChest() {
        return enderChest;
    }
}
