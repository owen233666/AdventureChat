package cn.owen233666.adventurechat.anvilprocessor;

import cn.owen233666.adventurechat.utils.ComponentConverter;
import cn.owen233666.adventurechat.utils.convertutils;
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
            String nameStr = event.getName();
            nameStr =  convertutils.convertString(nameStr);
            name = MiniMessage.miniMessage().deserialize(nameStr);
        }
        net.minecraft.network.chat.Component mcComponent = ComponentConverter.convertToMinecraft(name, event.getPlayer().registryAccess());
        ItemStack output = event.getLeft().copy();
        output.set(DataComponents.CUSTOM_NAME, mcComponent);
        event.setOutput(output);
        event.setCost(3);
        event.setMaterialCost(1);
    }
}
