package cn.owen233666.adventurechat.network;

import cn.owen233666.adventurechat.client.FakeContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class OpenFakeContainer {
    public static void openFakeContainer(ServerPlayer owner, ServerPlayer viewer) {
        ItemStack displayItem = owner.getMainHandItem(); // 获取主手物品

        // 创建虚拟容器
        FakeContainerMenu menu = new FakeContainerMenu(
                getNextContainerCounter(viewer), // 获取下一个容器ID
                viewer.getInventory(), // 查看者的背包
                displayItem // 要显示的物品
        );

        // 发送打开容器数据包
        viewer.connection.send(new ClientboundOpenScreenPacket(
                menu.containerId,
                MenuType.GENERIC_9x3,
                Component.literal("查看 " + owner.getScoreboardName() + " 的手持物品")
        ));

        // 初始化容器
        viewer.containerMenu = menu;
        menu.addSlotListener((ContainerListener) viewer); // 监听容器变化（可选）
        menu.broadcastChanges(); // 更新物品显示
    }


}
