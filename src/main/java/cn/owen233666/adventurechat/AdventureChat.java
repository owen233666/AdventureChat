package cn.owen233666.adventurechat;

import cn.owen233666.adventurechat.anvilprocessor.AnvilUpdateEventHandler;
import cn.owen233666.adventurechat.serverchatprocessor.ServerChatEventHandler;
import cn.owen233666.adventurechat.commands.CommandRegister;
import cn.owen233666.adventurechat.network.Networking;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

@Mod(AdventureChat.MODID)
public class AdventureChat {
    public static final String MODID = "adventurechat";
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    public AdventureChat(IEventBus modEventBus, ModContainer modContainer) {
        // 注册配置
        Config.register(ModLoadingContext.get().getActiveContainer());
        modEventBus.addListener(Config::onConfigChanged);
        modEventBus.addListener(Networking::register);
        NeoForge.EVENT_BUS.addListener(CommandRegister::onRegisterCommands);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ServerChatEventHandler::onServerChat);
        NeoForge.EVENT_BUS.addListener(AnvilUpdateEventHandler::onAnvilUpdate);
    }
    public static ResourceLocation id(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}