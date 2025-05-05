package cn.owen233666.adventurechat.commands;

import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class CommandRegister {
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        AModCommands.register(event.getDispatcher());
    }
}
