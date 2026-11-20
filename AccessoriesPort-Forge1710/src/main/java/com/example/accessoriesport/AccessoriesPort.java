package com.example.accessoriesport;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = AccessoriesPort.MODID, name = "Accessories Port", version = "0.1.0-dev", acceptableRemoteVersions = "*")
public class AccessoriesPort {
    public static final String MODID = "accessoriesport";
    public static SimpleNetworkWrapper NETWORK;
    @SidedProxy(clientSide = "com.example.accessoriesport.ClientProxy", serverSide = "com.example.accessoriesport.CommonProxy")
    public static CommonProxy PROXY;

    public AccessoriesPort() { AccessoriesPortHolder.INSTANCE = this; }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        NETWORK.registerMessage(OpenCosmeticGuiMessage.Handler.class, OpenCosmeticGuiMessage.class, 0, Side.SERVER);
        NETWORK.registerMessage(SyncCosmeticArmorMessage.Handler.class, SyncCosmeticArmorMessage.class, 1, Side.CLIENT);
        NETWORK.registerMessage(ToggleCosmeticDisplayMessage.Handler.class, ToggleCosmeticDisplayMessage.class, 2, Side.SERVER);
        MinecraftForge.EVENT_BUS.register(new PlayerDataEvents());
        FMLCommonHandler.instance().bus().register(new PlayerSyncEvents());
        PROXY.registerClient();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new CosmeticGuiHandler());
    }
}
