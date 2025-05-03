package cn.owen233666.adventurechat;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = AdventureChat.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> HELP_STRING;
    public static final ModConfigSpec SPEC;

    static {
        BUILDER.push("general");

        HELP_STRING = BUILDER
                .comment("An example string configuration")
                .define("Instruction", "欢迎使用<gradient:>AdventureChat!以下是AdventureChat的大致使用方法:<newline>在目标内容之前输入\"<gradient:颜色1:颜色2>\"可以给目标内容加上渐变色，例如：<newline><gradient:#AABCDF:#1A6C5F>这是一段文字<reset>");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    // 新的注册方式
    public static void register(final ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, SPEC, "AdventureChat.toml");
    }

    // 可选：监听配置变化事件
    @SubscribeEvent
    public static void onConfigChanged(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            // 配置发生变化时的处理逻辑
        }
    }
}
