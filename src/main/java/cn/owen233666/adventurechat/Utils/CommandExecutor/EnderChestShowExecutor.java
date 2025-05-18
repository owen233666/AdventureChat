package cn.owen233666.adventurechat.Utils.CommandExecutor;

import cn.owen233666.adventurechat.Utils.Cache.EnderChestShowCache;
import cn.owen233666.adventurechat.Utils.DataType.EnderChestData;
import cn.owen233666.adventurechat.Utils.LockedSlot;
import com.mojang.authlib.GameProfile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class EnderChestShowExecutor {
    public static int execute(CommandSourceStack source, String uuid) throws ExecutionException {
        if(source.isPlayer() && !(source.getPlayer() instanceof FakePlayer)){
            ServerPlayer player = source.getPlayer();
            EnderChestData enderChestData = EnderChestShowCache.cache.get(UUID.fromString(uuid));
            PlayerEnderChestContainer enderChest = enderChestData.getEnderChest();

            player.openMenu(new MenuProvider() {
                public @NotNull Component getDisplayName() {
                    return Component.empty()
                            .append(enderChestData.getPlayer().getScoreboardName())
                            .append(Component.translatable("title.playersenderchest"));
                }

                @Override
                public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                    SimpleContainer container = new SimpleContainer(36);
                    ItemStack glassPane = new ItemStack(Items.WHITE_STAINED_GLASS_PANE);
                    glassPane.remove(DataComponents.CUSTOM_NAME); // 移除自定义名称
                    glassPane.remove(DataComponents.ITEM_NAME);  // 移除物品默认名称（如有）
                    glassPane.remove(DataComponents.LORE);         // 移除 Lore 文本
                    glassPane.remove(DataComponents.HIDE_TOOLTIP); // 确保不禁用 Tooltip（反向操作）
                    glassPane.set(DataComponents.CUSTOM_NAME, Component.empty()); // 设置自定义名称为空

                    ItemStack playerHead = new ItemStack(Items.PLAYER_HEAD);
                    GameProfile profile = new GameProfile(enderChestData.getPlayer().getUUID(), enderChestData.getPlayer().getScoreboardName());
                    ResolvableProfile resolvableProfile = new ResolvableProfile(profile);
                    playerHead.set(DataComponents.PROFILE, resolvableProfile);
                    playerHead.set(DataComponents.CUSTOM_NAME, Component.literal(enderChestData.getPlayer().getScoreboardName())); // 设置自定义名称为空

                    for (int i = 0; i < 36; i++) {
                        if(i <= 26){
                            container.setItem(i, enderChest.getItem(i).copy());
                        }
                        if(i >= 27 && i <= 34){
                            container.setItem(i, glassPane.copy());
                        }
                        if(i == 35){
                            container.setItem(i, playerHead.copy());
                        }
                    }
                    return new ChestMenu(MenuType.GENERIC_9x4, containerId, playerInventory, container, 4) {
                        @Override
                        public @NotNull Slot addSlot(@NotNull Slot slot) {
                            // 替换所有容器槽为 LockedSlot（禁止交互）
                            if (slot.container != playerInventory) {
                                return super.addSlot(new LockedSlot(slot.container, slot.getSlotIndex(), slot.x, slot.y));
                            }
                            return super.addSlot(slot); // 玩家背包槽保持正常
                        }

                        @Override
                        public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
                            return ItemStack.EMPTY;
                        }
                    };
                }
            });
        }
        return 0;
    }
}
