package cn.owen233666.adventurechat.utils;

import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

public class serverAudienceInit {
    public class AudienceInit {
        private static volatile MinecraftServerAudiences ACAudience;

        // 注册到NeoForge事件总线
        public static void register(IEventBus modEventBus) {
            NeoForge.EVENT_BUS.register(AudienceInit.class);
        }

        // 获取当前Audience提供者（线程安全）
        public static MinecraftServerAudiences adventure() {
            if (ACAudience == null) {
                throw new IllegalStateException("Adventure未初始化！服务器未运行。");
            }
            return ACAudience;
        }

        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            ACAudience = MinecraftServerAudiences.of(event.getServer());
            sendStartupMessage(event.getServer());
        }

        @SubscribeEvent
        public static void onServerStopped(ServerStoppedEvent event) {
            if (ACAudience != null) {
                ACAudience.close();
                ACAudience = null;
            }
        }

        private static void sendStartupMessage(MinecraftServer server) {
            // 示例：向控制台发送启动消息
            adventure().console().sendMessage(
                    Component.text("[AdventureChat] 服务器已启动！", NamedTextColor.GOLD)
            );
        }
    }
}
