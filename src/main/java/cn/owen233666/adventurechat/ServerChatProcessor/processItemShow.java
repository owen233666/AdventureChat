package cn.owen233666.adventurechat.ServerChatProcessor;

import cn.owen233666.adventurechat.utils.Cache.ItemShowCache;
import cn.owen233666.adventurechat.utils.DataType.ItemData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class processItemShow {
    public static String processItemShow(ServerPlayer player, String message) {
        Pattern p = Pattern.compile("%i(?!nv)");
        Matcher matcher = p.matcher(message);
        if (!matcher.find()) return message;
        UUID uuid = UUID.randomUUID();
        ItemData itemData = new ItemData().setItem(player.getMainHandItem()).setPlayer(player);
        ItemStack heldItem = player.getMainHandItem();
        //写入缓存
        ItemShowCache.cache.put(uuid, itemData);

        String name = heldItem.getItem().getDefaultInstance().getHoverName().getString();

        String itemDisplay = "[<click:run_command:'/adventurechat previewitem "+ uuid +"'>" +"<lang:" + name+ ">" + "<reset>]";

        String processed = p.matcher(message).replaceAll(itemDisplay);
        return processed;
    }
}
