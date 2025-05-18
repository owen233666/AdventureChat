// ScreenEventListener.java
package cn.owen233666.adventurechat.client;

import cn.owen233666.adventurechat.AdventureChat;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AdventureChat.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ScreenEventListener {

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        Screen screen = event.getScreen();
        // 当打开的是普通ChatScreen且不是我们的CustomChatScreen时替换
        if (screen instanceof ChatScreen && !(screen instanceof ToggleButton)) {
            event.setNewScreen(new ToggleButton(""));
        }
    }
}