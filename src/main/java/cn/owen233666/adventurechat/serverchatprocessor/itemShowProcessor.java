package cn.owen233666.adventurechat.serverchatprocessor;

import cn.owen233666.adventurechat.utils.Cache.ItemShowCache;
import cn.owen233666.adventurechat.utils.DataType.ItemData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class itemShowProcessor {
    public static Component processItemShow(ServerPlayer player, Component message) {
        String message1 = LegacyComponentSerializer.legacyAmpersand().serialize(message);
        Pattern p = Pattern.compile("%i(?!nv)");
        Matcher matcher = p.matcher(message1);
        HolderLookup.Provider registries = null;
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            registries = ServerLifecycleHooks.getCurrentServer().registryAccess();
        }

        if (!matcher.find()) return message;

        UUID uuid = UUID.randomUUID();
        ItemData itemData = new ItemData().setItem(player.getMainHandItem()).setPlayer(player);
        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.get(DataComponents.CUSTOM_NAME) == null) {
            Rarity rarity = heldItem.getRarity();
            String color = switch (rarity){
                case UNCOMMON -> "<yellow>";
                case RARE -> "<aqua>";
                case EPIC -> "<light_purple>";
                default -> "<white>";
            };
            ItemShowCache.cache.put(uuid, itemData);

            String name = heldItem.getItem().getDescriptionId();
            DataComponentMap itemComponent = heldItem.getComponents();
            HoverEvent.ShowItem item = HoverEvent.ShowItem.showItem();

            Component processed = message.replaceText(config -> config.match("%i(?!nv)")
                    .replacement(Component.text(name)
                            .hoverEvent())); //ShowItemHoverEvent
//            String itemDisplay = color + "[<click:run_command:'/adventurechat previewitem " + uuid + "'>" + "<lang:" + name + ">" + "]<reset>";
//            String processed = p.matcher(message1).replaceAll(itemDisplay);
//            return processed;
        } else {
            Component CUSTOM_NAME = heldItem.get(DataComponents.CUSTOM_NAME);
        }
        return message;
    }
}
