package com.example.accessoriesport;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class CosmeticKeyHandler {
    public static final KeyBinding OPEN = new KeyBinding("key.accessoriesport.open", Keyboard.KEY_O, "key.categories.inventory");
    static { ClientRegistry.registerKeyBinding(OPEN); }

    @SubscribeEvent public void onKey(InputEvent.KeyInputEvent event) {
        if (OPEN.isPressed() && Minecraft.getMinecraft().thePlayer != null) {
            AccessoriesPort.NETWORK.sendToServer(new OpenCosmeticGuiMessage());
        }
    }
}
