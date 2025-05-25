package cn.owen233666.adventurechat.serverchatprocessor;

import cn.owen233666.adventurechat.utils.Cache.InventoryShowCache;
import cn.owen233666.adventurechat.utils.DataType.InventoryData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class inventoryShowProcessor {
    public static String processInvtoryShow(ServerPlayer player, String message) {
        Pattern p1 = Pattern.compile("%inv");
        Pattern p2 = Pattern.compile("\\[inv]");
        Matcher m1 = p1.matcher(message);
        Matcher m2 = p2.matcher(message);
        if (!m1.find() && !m2.find()) return message;
        UUID uuid = UUID.randomUUID();
        Inventory inventory = player.getInventory();
        InventoryData inventoryData = new InventoryData().setInventory(player, inventory);
        //写入缓存
        InventoryShowCache.cache.put(uuid, inventoryData);

        String itemDisplay = "<aqua>[<click:run_command:'/adventurechat previewinventory "+ uuid +"'>" + player.getScoreboardName() + "<lang:hover.show.backpack>" + "]<reset>";

        String processed;
        processed = p1.matcher(message).replaceAll(itemDisplay);
        processed = p2.matcher(processed).replaceAll(itemDisplay);
        return processed;
    }
}
