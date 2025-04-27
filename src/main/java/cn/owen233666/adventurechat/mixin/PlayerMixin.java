// src/main/java/cn/owen233666/adventurechat/mixin/PlayerMixin.java
package cn.owen233666.adventurechat.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Player.class)
public abstract class PlayerMixin {

    /**
     * 完全覆盖原版名称格式化逻辑
     * @reason 强制移除所有团队前缀/后缀
     */
    @Overwrite
    public Component getDisplayName() {
        Player self = (Player)(Object)this;

        // 1. 获取原始名称
        String rawName = self.getScoreboardName();

        // 2. 创建纯文本组件（无团队格式）
        MutableComponent name = Component.literal(rawName);

//        // 3. 保留悬停文本（可选）
//        name.withStyle(style -> style.withHoverEvent(
//                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
//                        Component.literal("玩家: " + rawName))
//        );

        return name;
    }
}