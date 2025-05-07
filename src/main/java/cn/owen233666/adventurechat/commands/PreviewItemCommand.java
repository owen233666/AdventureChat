package cn.owen233666.adventurechat.commands;

import cn.owen233666.adventurechat.utils.show.ItemShow;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class PreviewItemCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            ItemShow.openPreviewMenu(player, player.getMainHandItem());
        }
        return 1;
    }
}