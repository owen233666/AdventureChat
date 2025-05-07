package cn.owen233666.adventurechat.utils.show;

import cn.owen233666.adventurechat.AdventureChat;
import cn.owen233666.adventurechat.utils.ItemPreviewMenu;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;

import java.util.Collections;

public class ItemShow {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onServerChat(ServerChatEvent event) {
        if (!event.getRawText().contains("%i")) return;

        ServerPlayer player = event.getPlayer();
        ItemStack heldItem = player.getMainHandItem();

        if (heldItem.isEmpty()) {
            event.setMessage(net.minecraft.network.chat.Component.literal(event.getMessage().toString().replace("%i", "[空手]")));
            return;
        }

        String itemDisplay = createItemDisplay(heldItem);
        event.setMessage(net.minecraft.network.chat.Component.literal(event.getMessage().toString().replace("%i", itemDisplay)));
    }

    private static String createItemDisplay(ItemStack item) {
        Key itemKey = Key.key(
                item.getItem().builtInRegistryHolder().key().location().getNamespace(),
                item.getItem().builtInRegistryHolder().key().location().getPath()
        );

        // 构建完整的 MiniMessage 格式字符串
        return "<hover:show_item:'" + itemKey + "'," + item.getCount() + ">" +
                "<click:run_command:'/adventurechat previewitem'>" +
                item.getHoverName().getString() +
                "</click></hover>";
    }

    private static Component createItemComponent(ItemStack item) {
        // 使用ItemStack的getHoverName()方法获取显示名称
        Component displayName = GsonComponentSerializer.gson().deserialize(String.valueOf(item.getHoverName()));

        // 创建HoverEvent展示物品信息 - 使用最新的dataComponents API
        Key itemKey = Key.key(
                item.getItem().builtInRegistryHolder().key().location().getNamespace(),
                item.getItem().builtInRegistryHolder().key().location().getPath()
        );

        HoverEvent<HoverEvent.ShowItem> hoverEvent = HoverEvent.showItem(
                HoverEvent.ShowItem.showItem(
                        itemKey,
                        item.getCount(),
                        Collections.emptyMap() // 使用空的数据组件映射
                )
        );

        // 创建ClickEvent打开预览菜单
        ClickEvent clickEvent = ClickEvent.runCommand("/adventurechat previewitem");

        // 构建最终组件
        return Component.text()
                .content(displayName.toString())
                .hoverEvent(hoverEvent)
                .clickEvent(clickEvent)
                .build();
    }

    public static void openPreviewMenu(ServerPlayer player, ItemStack item) {
        if (item.isEmpty()) return;

        player.openMenu(new SimpleMenuProvider(
                (id, inv, p) -> new ItemPreviewMenu(id, inv, item),
                item.getHoverName()
        ));
    }
}