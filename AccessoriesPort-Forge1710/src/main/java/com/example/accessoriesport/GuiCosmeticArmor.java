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
    private IconGuiButton displayButton;

    public GuiCosmeticArmor(EntityPlayer player) {
        super(new CosmeticArmorContainer(player));
        this.player = player;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void initGui() {
        super.initGui();
        // Eye of Ender icon toggles whether functional armor is rendered when
        // the matching cosmetic slot is empty. The full button stays in-panel.
        displayButton = new IconGuiButton(0, guiLeft + 148, guiTop + 7, 20, 20,
            IconGuiButton.ICON_EYE);
        buttonList.add(displayButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            CosmeticArmorData data = CosmeticArmorData.get(player);
            if (data != null) {
                data.setShowFunctionalArmorWhenEmpty(!data.shouldShowFunctionalArmorWhenEmpty());
                AccessoriesPort.NETWORK.sendToServer(
                    new ToggleCosmeticDisplayMessage(data.shouldShowFunctionalArmorWhenEmpty()));
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        // Keep labels short and inside the narrow left column.
        fontRendererObj.drawString("Head", 28, 13, 0x404040);
        fontRendererObj.drawString("Chest", 28, 35, 0x404040);
        fontRendererObj.drawString("Legs", 28, 57, 0x404040);
        fontRendererObj.drawString("Feet", 28, 79, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(BACKGROUND);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        GuiInventory.func_147046_a(guiLeft + 112, guiTop + 78, 30,
            (float)(guiLeft + 112) - mouseX, (float)(guiTop + 45) - mouseY, player);
    }
}
