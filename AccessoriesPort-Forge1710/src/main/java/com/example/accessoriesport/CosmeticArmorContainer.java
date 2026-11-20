package com.example.accessoriesport;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class CosmeticArmorContainer extends Container {
    private final CosmeticArmorData cosmetic;
    private final EntityPlayer player;

    public CosmeticArmorContainer(EntityPlayer player) {
        this.player = player;
        this.cosmetic = CosmeticArmorData.get(player);

        // Cosmetic armor slots.
        for (int i = 0; i < 4; i++) {
            final int armorType = i;
            addSlotToContainer(new Slot(cosmetic, i, 8, 8 + i * 22) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return cosmetic.isItemValidForSlot(armorType, stack);
                }

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }
            });
        }

        // Vanilla player inventory layout. These coordinates must not overlap:
        // main inventory rows at y=84, 102, 120; hotbar at y=142.
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(player.inventory,
                    col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(player.inventory,
                col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return player == this.player;
    }

    // Slot indices: 0-3 cosmetic, 4-30 main inventory, 31-39 hotbar.
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        if (index < 0 || index >= inventorySlots.size()) return null;

        Slot sourceSlot = (Slot) inventorySlots.get(index);
        if (sourceSlot == null || !sourceSlot.getHasStack()) return null;

        ItemStack source = sourceSlot.getStack();
        ItemStack original = source.copy();
        boolean moved = false;

        if (index < 4) {
            // Move cosmetic armor back into the player's inventory.
            moved = mergeItemStack(source, 4, 40, true);
        } else {
            // Try the matching empty cosmetic slot first.
            if (source.getItem() instanceof ItemArmor) {
                int armorType = ((ItemArmor) source.getItem()).armorType;
                if (armorType >= 0 && armorType < 4) {
                    Slot cosmeticSlot = getSlot(armorType);
                    if (!cosmeticSlot.getHasStack() && cosmeticSlot.isItemValid(source)) {
                        moved = mergeItemStack(source, armorType, armorType + 1, false);
                    }
                }
            }

            // If it cannot be equipped cosmetically, move between the main
            // inventory and hotbar just like a normal player inventory.
            if (!moved) {
                if (index >= 4 && index < 31) {
                    moved = mergeItemStack(source, 31, 40, false);
                } else if (index >= 31 && index < 40) {
                    moved = mergeItemStack(source, 4, 31, false);
                }
            }
        }

        if (!moved) return null;

        if (source.stackSize <= 0) {
            sourceSlot.putStack(null);
        } else {
            sourceSlot.onSlotChanged();
        }

        if (source.stackSize == original.stackSize) return null;
        sourceSlot.onPickupFromSlot(player, source);
        return original;
    }
}
