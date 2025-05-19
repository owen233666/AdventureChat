package cn.owen233666.adventurechat.utils;

import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.HolderLookup;
import com.mojang.serialization.DataResult;
import java.util.Optional;

public class ItemStackToSNBT {
    public static String toSNBT(ItemStack stack, HolderLookup.Provider lookupProvider) {
        if (stack.isEmpty()) {
            return "{}"; // 空物品堆栈返回空NBT
        }

        // 使用ItemStack的CODEC进行编码
        DataResult<Tag> result = ItemStack.CODEC.encodeStart(
                RegistryOps.create(NbtOps.INSTANCE, lookupProvider),
                stack
        );

        // 处理编码结果
        Optional<Tag> optionalTag = result.resultOrPartial(error -> {
            System.err.println("Failed to encode ItemStack: " + error);
        });

        // 出错时返回空NBT
        // 将NBT Tag转换为SNBT字符串
        return optionalTag.map(tag -> NbtOps.INSTANCE.convertTo(NbtOps.INSTANCE, tag).toString()).orElse("{}");

    }
}