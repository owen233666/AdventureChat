package cn.owen233666.adventurechat.Commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public class AModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // 使用命令构建器
        dispatcher.register(CommandBuilder.buildAModCommand());
    }
}