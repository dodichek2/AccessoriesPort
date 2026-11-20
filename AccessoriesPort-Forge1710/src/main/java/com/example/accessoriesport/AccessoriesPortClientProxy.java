package com.example.accessoriesport;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public final class AccessoriesPortClientProxy {
    private AccessoriesPortClientProxy() { }
    public static EntityPlayer getClientPlayer() { return Minecraft.getMinecraft().thePlayer; }
}
