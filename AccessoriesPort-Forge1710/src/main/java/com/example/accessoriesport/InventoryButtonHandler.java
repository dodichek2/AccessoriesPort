package com.example.accessoriesport;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiScreenEvent;

public class InventoryButtonHandler {
    private static final int BUTTON_ID = 17410;

    @SubscribeEvent
    public void onInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.gui instanceof GuiInventory)) return;
        int left = (event.gui.width - 176) / 2;
        int top = (event.gui.height - 166) / 2;

        // Diamond helmet icon opens the cosmetic armor page.
        event.buttonList.add(new IconGuiButton(BUTTON_ID, left + 153, top + 4,
            18, 18, IconGuiButton.ICON_HELMET));
    }

    @SubscribeEvent
    public void onAction(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.gui instanceof GuiInventory && event.button.id == BUTTON_ID) {
            event.setCanceled(true);
            AccessoriesPort.NETWORK.sendToServer(new OpenCosmeticGuiMessage());
        }
    }
}
