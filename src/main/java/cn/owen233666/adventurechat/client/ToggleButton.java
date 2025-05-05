package cn.owen233666.adventurechat.client;

import cn.owen233666.adventurechat.AdventureChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ToggleButton extends ChatScreen {
    // 两种状态的贴图资源
    private static final ResourceLocation BUTTON_ON_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(AdventureChat.MODID, "textures/togglebutton/button_on.png");
    private static final ResourceLocation BUTTON_OFF_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(AdventureChat.MODID, "textures/togglebutton/button_off.png");
    private int buttonX, buttonY;
    private final int buttonWidth = 128;
    private final int buttonHeight = 24;
    private final Component buttonOnText = Component.literal("AdventureAPI解析已启用");
    private final Component buttonOffText = Component.literal("AdventureAPI解析已关闭");
    private boolean isButtonOn = true;

    public ToggleButton(String initial) {
        super(initial);
    }

    @Override
    protected void init() {
        super.init();
        buttonX = this.width - buttonWidth - 10;
        buttonY = this.height - 37;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // 根据状态选择贴图
        ResourceLocation currentTexture = isButtonOn ? BUTTON_ON_TEXTURE : BUTTON_OFF_TEXTURE;

        // 渲染当前状态的贴图
        guiGraphics.blit(
                currentTexture,
                buttonX, buttonY,
                0, 0,
                buttonWidth, buttonHeight,
                buttonWidth, buttonHeight
        );

        String TextBuffer;
        if(isButtonOn){
            TextBuffer = buttonOnText.getString();
        }else{
            TextBuffer = buttonOffText.getString();
        }
        // 渲染文字（带状态颜色）
        int textWidth = Minecraft.getInstance().font.width(TextBuffer);
        int textX = buttonX + (buttonWidth - textWidth) / 2;
        int textY = buttonY + (buttonHeight - 8) / 2;
        guiGraphics.drawString(Minecraft.getInstance().font, TextBuffer, textX, textY, isButtonOn ? 0x00FF00 : 0xFF0000, true);// 开启时绿色

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 &&
                mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {

            // 切换按钮状态
            isButtonOn = !isButtonOn;

            if(isButtonOn) {
//                this.handleChatInput("/neotest on", true);
            } else {
//                this.handleChatInput("/neotest off", true);
            }

            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}