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

    @Override public void initGui() {
        super.initGui();
        CosmeticArmorData data = CosmeticArmorData.get(player);
        displayButton = new GuiButton(0, guiLeft + 94, guiTop + 8, 74, 20, buttonText(data != null && data.shouldShowFunctionalArmorWhenEmpty()));
        buttonList.add(displayButton);
    }

    private String buttonText(boolean show) { return show ? "Vanilla: ON" : "Vanilla: OFF"; }

    @Override protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            CosmeticArmorData data = CosmeticArmorData.get(player);
            if (data != null) {
                data.setShowFunctionalArmorWhenEmpty(!data.shouldShowFunctionalArmorWhenEmpty());
                displayButton.displayString = buttonText(data.shouldShowFunctionalArmorWhenEmpty());
                AccessoriesPort.NETWORK.sendToServer(new ToggleCosmeticDisplayMessage(data.shouldShowFunctionalArmorWhenEmpty()));
            }
        }
    }

    @Override protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Cosmetic Armor", 28, 2, 0x404040);
        fontRendererObj.drawString("Helmet", 30, 13, 0x404040);
        fontRendererObj.drawString("Chest", 30, 35, 0x404040);
        fontRendererObj.drawString("Leggings", 30, 57, 0x404040);
        fontRendererObj.drawString("Boots", 30, 79, 0x404040);
    }

    @Override protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(BACKGROUND);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        GuiInventory.func_147046_a(guiLeft + 80, guiTop + 78, 30, (float)(guiLeft + 80) - mouseX, (float)(guiTop + 45) - mouseY, player);
    }
}
