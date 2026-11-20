package com.example.accessoriesport;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCosmeticArmor extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation("accessoriesport", "textures/gui/cosmetic_armor.png");
    private final EntityPlayer player;
    private GuiButton displayButton;

    public GuiCosmeticArmor(EntityPlayer player) {
        super(new CosmeticArmorContainer(player));
        this.player = player;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void initGui() {
        super.initGui();
        CosmeticArmorData data = CosmeticArmorData.get(player);

        // Keep the toggle fully inside the 176px-wide GUI.
        displayButton = new GuiButton(0, guiLeft + 104, guiTop + 8, 64, 18,
            buttonText(data != null && data.shouldShowFunctionalArmorWhenEmpty()));
        buttonList.add(displayButton);
    }

    private String buttonText(boolean show) {
        return show ? "Show: ON" : "Show: OFF";
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            CosmeticArmorData data = CosmeticArmorData.get(player);
            if (data != null) {
                data.setShowFunctionalArmorWhenEmpty(!data.shouldShowFunctionalArmorWhenEmpty());
                displayButton.displayString = buttonText(data.shouldShowFunctionalArmorWhenEmpty());
                AccessoriesPort.NETWORK.sendToServer(
                    new ToggleCosmeticDisplayMessage(data.shouldShowFunctionalArmorWhenEmpty()));
            }
        }
    }

    private void drawFittedLabel(String text, int x, int y, int maxWidth) {
        String fitted = fontRendererObj.trimStringToWidth(text, maxWidth);
        fontRendererObj.drawString(fitted, x, y, 0x404040);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        // Short labels stay in the narrow strip between the cosmetic slots and preview.
        drawFittedLabel("Head", 28, 13, 38);
        drawFittedLabel("Chest", 28, 35, 38);
        drawFittedLabel("Legs", 28, 57, 38);
        drawFittedLabel("Feet", 28, 79, 38);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(BACKGROUND);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        // Move the preview right so it does not collide with the slot labels.
        GuiInventory.func_147046_a(guiLeft + 112, guiTop + 78, 30,
            (float)(guiLeft + 112) - mouseX, (float)(guiTop + 45) - mouseY, player);
    }
}
