package com.example.accessoriesport;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor;

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

    /**
     * Keep vanilla-style inventory transfer behavior while the cosmetic tab is open.
     * Slot indices: 0-3 cosmetic armor, 4-30 main inventory, 31-39 hotbar.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        if (index < 0 || index >= inventorySlots.size()) return null;

        ItemStack original = null;
        Slot sourceSlot = (Slot) inventorySlots.get(index);
        if (sourceSlot == null || !sourceSlot.getHasStack()) return null;

        ItemStack source = sourceSlot.getStack();
        original = source.copy();

        if (index < 4) {
            // Take cosmetic armor back into the player's inventory.
            if (!mergeItemStack(source, 4, 40, true)) return null;
        } else {
            boolean moved = false;

            // Shift-click armor into its matching cosmetic slot first.
            if (source.getItem() instanceof ItemArmor) {
                int armorType = ((ItemArmor) source.getItem()).armorType;
                if (armorType >= 0 && armorType < 4 &&
                    getSlot(armorType).isItemValid(source) &&
                    getSlot(armorType).getHasStack() == false) {
                    moved = mergeItemStack(source, armorType, armorType + 1, false);
                }
            }

            // If it did not fit cosmetically, preserve normal main-inventory/hotbar
            // shift-click behavior instead of trying to merge into the source range.
            if (!moved) {
                if (index >= 4 && index < 31) {
                    moved = mergeItemStack(source, 31, 40, false);
                } else if (index >= 31 && index < 40) {
                    moved = mergeItemStack(source, 4, 31, false);
                }
            }
            if (!moved) return null;
        }

        if (source.stackSize == 0) {
            sourceSlot.putStack(null);
        } else {
            sourceSlot.onSlotChanged();
        }
        if (source.stackSize == original.stackSize) return null;
        sourceSlot.onPickupFromSlot(player, source);
        return original;
    }
}
