package cn.owen233666.adventurechat.utils.DataType;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ItemData {
    private Player player;
    private ItemStack itemstack;

    public ItemData setItem(ItemStack itemstack){
        this.itemstack = itemstack;
        return this;
    }
    public ItemData setPlayer(@Nullable Player player){
        this.player = player;
        return this;
    }
    public ItemStack getItem(){
        return itemstack;
    }

    public Player getPlayer() {
        return player;
    }
}
