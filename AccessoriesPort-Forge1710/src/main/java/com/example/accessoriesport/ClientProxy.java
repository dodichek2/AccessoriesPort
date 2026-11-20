package com.example.accessoriesport;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override public void registerClient() {
        MinecraftForge.EVENT_BUS.register(new CosmeticArmorRenderHandler());
        MinecraftForge.EVENT_BUS.register(new InventoryButtonHandler());
        FMLCommonHandler.instance().bus().register(new CosmeticKeyHandler());
    }
}
