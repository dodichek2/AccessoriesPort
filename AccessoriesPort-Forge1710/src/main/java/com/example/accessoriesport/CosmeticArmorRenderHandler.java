package com.example.accessoriesport;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/** Temporarily swaps the client-side armor array only during player rendering. */
public class CosmeticArmorRenderHandler {
    private final Map<Integer, Stack<ItemStack[]>> originals = new HashMap<Integer, Stack<ItemStack[]>>();

    @SubscribeEvent
    public void onRenderPre(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.entityPlayer;
        CosmeticArmorData data = CosmeticArmorData.get(player);
        if (data == null) return;
        int id = player.getEntityId();
        ItemStack[] original = new ItemStack[4];
        for (int i = 0; i < 4; i++) original[i] = player.inventory.armorInventory[i];
        Stack<ItemStack[]> stack = originals.get(id);
        if (stack == null) { stack = new Stack<ItemStack[]>(); originals.put(id, stack); }
        stack.push(original);
        for (int i = 0; i < 4; i++) {
            // Armor inventory order is boots, leggings, chestplate, helmet; accessory UI order is the reverse.
            ItemStack cosmetic = data.getCosmetic(3 - i);
            if (cosmetic != null) player.inventory.armorInventory[i] = cosmetic;
            else if (!data.shouldShowFunctionalArmorWhenEmpty()) player.inventory.armorInventory[i] = null;
        }
    }

    @SubscribeEvent
    public void onRenderPost(RenderPlayerEvent.Post event) {
        restore(event.entityPlayer);
    }

    private void restore(EntityPlayer player) {
        Stack<ItemStack[]> stack = originals.get(player.getEntityId());
        if (stack == null || stack.isEmpty()) return;
        ItemStack[] original = stack.pop();
        if (stack.isEmpty()) originals.remove(player.getEntityId());
        for (int i = 0; i < 4; i++) player.inventory.armorInventory[i] = original[i];
    }
}
