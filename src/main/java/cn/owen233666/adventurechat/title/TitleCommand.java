package cn.owen233666.adventurechat.title;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class TitleCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String title = context.getArgument("title", String.class);

        TitleSystem.setPlayerTitle(player, title);
        player.sendSystemMessage(Component.literal("称号设置成功！"));
        return SINGLE_SUCCESS;
    }
}