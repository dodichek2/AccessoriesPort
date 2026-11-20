package com.example.accessoriesport;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;

/** Sends the joining/respawning player's own cosmetic state to that player's client. */
public class PlayerSyncEvents {
    @SubscribeEvent public void onLogin(PlayerEvent.PlayerLoggedInEvent event) { sync(event.player); }
    @SubscribeEvent public void onRespawn(PlayerEvent.PlayerRespawnEvent event) { sync(event.player); }
    @SubscribeEvent public void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) { sync(event.player); }

    private void sync(net.minecraft.entity.player.EntityPlayer player) {
        if (player instanceof EntityPlayerMP && AccessoriesPort.NETWORK != null) {
            AccessoriesPort.NETWORK.sendTo(new SyncCosmeticArmorMessage(player), (EntityPlayerMP) player);
        }
    }
}
