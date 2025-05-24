package cn.owen233666.adventurechat.anvilprocessor;

import cn.owen233666.adventurechat.utils.ComponentConverter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

public class AnvilUpdateEventHandler {
    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        Component name = null;
        if (event.getName() != null) {
            name = MiniMessage.miniMessage().deserialize(event.getName());
        }

        net.minecraft.network.chat.Component mcComponent = ComponentConverter.convertToMinecraft(name, event.getPlayer().registryAccess());

        // 5. 创建输出物品并设置名称
        ItemStack output = event.getLeft().copy();
        output.set(DataComponents.CUSTOM_NAME, mcComponent);

        // 6. 更新事件参数
        event.setOutput(output); // 设置输出物品
        event.setCost(3); // 设置经验消耗
        event.setMaterialCost(1); // 设置材料消耗

    }
}
