package com.example.accessoriesport;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerDataEvents {
    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer && event.entity.getExtendedProperties(CosmeticArmorData.KEY) == null) {
            event.entity.registerExtendedProperties(CosmeticArmorData.KEY, new CosmeticArmorData((EntityPlayer) event.entity));
        }
    }

    @SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if (!event.world.isRemote && event.entity instanceof EntityPlayer && AccessoriesPort.NETWORK != null) {
            AccessoriesPort.NETWORK.sendToAll(new SyncCosmeticArmorMessage((EntityPlayer) event.entity));
        }
    }

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.target instanceof EntityPlayer && event.entityPlayer instanceof EntityPlayerMP && AccessoriesPort.NETWORK != null) {
            AccessoriesPort.NETWORK.sendTo(new SyncCosmeticArmorMessage((EntityPlayer) event.target), (EntityPlayerMP) event.entityPlayer);
        }
    }

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        CosmeticArmorData oldData = CosmeticArmorData.get(event.original);
        CosmeticArmorData newData = CosmeticArmorData.get(event.entityPlayer);
        if (newData != null) newData.copyFrom(oldData);
    }
}
