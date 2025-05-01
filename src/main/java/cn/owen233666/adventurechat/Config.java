package cn.owen233666.adventurechat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = AdventureChat.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> HELP_STRING;
    public static final ModConfigSpec SPEC;

    static {
        BUILDER.push("general");

        HELP_STRING = BUILDER
                .comment("An example string configuration")
                .define("exampleString", "defaultValue");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    // 新的注册方式
    public static void register(final ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, SPEC, "examplemod.toml");
    }

    // 可选：监听配置变化事件
    @SubscribeEvent
    public static void onConfigChanged(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            // 配置发生变化时的处理逻辑
        }
    }
}
