// src/main/java/cn/owen233666/adventurechat/mixin/PlayerMixin.java
package cn.owen233666.adventurechat.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(
            method = "getDisplayName",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGetDisplayName(CallbackInfoReturnable<Component> cir) {
        Player player = (Player)(Object)this;
        // 直接返回玩家名而不加<Dev>前缀
        cir.setReturnValue(Component.literal(player.getScoreboardName()));
    }
}