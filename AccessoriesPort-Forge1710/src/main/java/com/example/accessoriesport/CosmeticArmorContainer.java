package com.example.accessoriesport;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CosmeticArmorContainer extends Container {
    private final CosmeticArmorData cosmetic;
    private final EntityPlayer player;

    public CosmeticArmorContainer(EntityPlayer player) {
        this.player = player;
        this.cosmetic = CosmeticArmorData.get(player);
        for (int i = 0; i < 4; i++) {
            final int armorType = i;
            addSlotToContainer(new Slot(cosmetic, i, 8, 8 + i * 22) {
                @Override public boolean isItemValid(ItemStack stack) { return cosmetic.isItemValidForSlot(armorType, stack); }
                @Override public int getSlotStackLimit() { return 1; }
            });
        }
        // Player inventory: 27 main slots and 9 hotbar slots.
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++)
            addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 104 + row * 18));
        for (int col = 0; col < 9; col++)
            addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, 142));
    }

    @Override public boolean canInteractWith(EntityPlayer player) { return player == this.player; }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack original = null;
        Slot slot = (Slot) inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            original = stack.copy();
            if (index >= 4) {
                if (stack.getItem() instanceof net.minecraft.item.ItemArmor) {
                    int armorType = ((net.minecraft.item.ItemArmor)stack.getItem()).armorType;
                    if (!mergeItemStack(stack, armorType, armorType + 1, false)) return null;
                } else if (!mergeItemStack(stack, 4, inventorySlots.size(), false)) return null;
            } else if (!mergeItemStack(stack, 4, inventorySlots.size(), true)) return null;
            if (stack.stackSize == 0) slot.putStack(null); else slot.onSlotChanged();
            if (stack.stackSize == original.stackSize) return null;
            slot.onPickupFromSlot(player, stack);
        }
        return original;
    }
}
