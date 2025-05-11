package cn.owen233666.adventurechat.ServerChatProcessor;

import cn.owen233666.adventurechat.utils.Cache.EnderChestShowCache;
import cn.owen233666.adventurechat.utils.DataType.EnderChestData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.PlayerEnderChestContainer;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class processEnderChestShow {
    public static String processEnderChestShow(ServerPlayer player, String message) {
        Pattern p1 = Pattern.compile("%end");
        Pattern p2 = Pattern.compile("\\[end\\]");
        Matcher m1 = p1.matcher(message);
        Matcher m2 = p2.matcher(message);
        if (!m1.find() && !m2.find()) return message;
        UUID uuid = UUID.randomUUID();
        PlayerEnderChestContainer enderchest = player.getEnderChestInventory();
        EnderChestData enderChestData = new EnderChestData().setEnderChest(player, enderchest);
        //写入缓存
        EnderChestShowCache.cache.put(uuid, enderChestData);

        String enderChestDisplay = "<light_purple>[<click:run_command:'/adventurechat previewenderchest " + uuid + "'>" + player.getScoreboardName() + "<lang:hover.show.enderchest>" + "]<reset>";

        String processed;
        processed = p1.matcher(message).replaceAll(enderChestDisplay);
        processed = p2.matcher(processed).replaceAll(enderChestDisplay);
        return processed;
    }
}
