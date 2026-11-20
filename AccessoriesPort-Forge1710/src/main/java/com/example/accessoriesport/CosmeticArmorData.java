package com.example.accessoriesport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.EntityPlayer;

/** Four cosmetic armor pieces. These stacks never participate in armor calculations. */
public class CosmeticArmorData implements IInventory, net.minecraftforge.common.IExtendedEntityProperties {
    public static final String KEY = "AccessoriesPortCosmeticArmor";
    private final ItemStack[] stacks = new ItemStack[4];
    private final EntityPlayer player;
    private boolean showFunctionalArmorWhenEmpty = true;

    public CosmeticArmorData(EntityPlayer player) { this.player = player; }
    public static CosmeticArmorData get(Entity entity) {
        if (entity == null) return null;
        return (CosmeticArmorData) entity.getExtendedProperties(KEY);
    }
    public boolean shouldShowFunctionalArmorWhenEmpty() { return showFunctionalArmorWhenEmpty; }
    public void setShowFunctionalArmorWhenEmpty(boolean value) { showFunctionalArmorWhenEmpty = value; markDirty(); }
    public ItemStack getCosmetic(int armorType) { return armorType >= 0 && armorType < 4 ? stacks[armorType] : null; }

    @Override public void init(net.minecraft.entity.Entity entity, World world) { }

    @Override
    public void saveNBTData(NBTTagCompound root) {
        NBTTagCompound data = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < 4; i++) if (stacks[i] != null) {
            NBTTagCompound item = new NBTTagCompound();
            item.setByte("Slot", (byte)i);
            stacks[i].writeToNBT(item);
            list.appendTag(item);
        }
        data.setTag("Items", list);
        data.setBoolean("ShowFunctionalWhenEmpty", showFunctionalArmorWhenEmpty);
        root.setTag(KEY, data);
    }
    public void loadNBTData(NBTTagCompound root) {
        if (!root.hasKey(KEY)) return;
        NBTTagCompound data = root.getCompoundTag(KEY);
        NBTTagList list = data.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound item = list.getCompoundTagAt(i);
            int slot = item.getByte("Slot");
            if (slot >= 0 && slot < 4) stacks[slot] = ItemStack.loadItemStackFromNBT(item);
        }
        showFunctionalArmorWhenEmpty = !data.hasKey("ShowFunctionalWhenEmpty") || data.getBoolean("ShowFunctionalWhenEmpty");
    }
    public void copyFrom(CosmeticArmorData other) {
        if (other == null) return;
        for (int i = 0; i < 4; i++) stacks[i] = other.stacks[i] == null ? null : other.stacks[i].copy();
        showFunctionalArmorWhenEmpty = other.showFunctionalArmorWhenEmpty;
    }

    @Override public int getSizeInventory() { return 4; }
    @Override public ItemStack getStackInSlot(int slot) { return slot >= 0 && slot < 4 ? stacks[slot] : null; }
    @Override public ItemStack decrStackSize(int slot, int amount) {
        if (slot < 0 || slot >= 4 || stacks[slot] == null) return null;
        ItemStack result;
        if (stacks[slot].stackSize <= amount) { result = stacks[slot]; stacks[slot] = null; }
        else { result = stacks[slot].splitStack(amount); if (stacks[slot].stackSize <= 0) stacks[slot] = null; }
        markDirty(); return result;
    }
    @Override public ItemStack getStackInSlotOnClosing(int slot) { if (slot < 0 || slot >= 4) return null; ItemStack s = stacks[slot]; stacks[slot] = null; return s; }
    @Override public void setInventorySlotContents(int slot, ItemStack stack) { if (slot >= 0 && slot < 4) { stacks[slot] = stack; if (stack != null && stack.stackSize > getInventoryStackLimit()) stack.stackSize = getInventoryStackLimit(); markDirty(); } }
    @Override public String getInventoryName() { return "container.accessoriesport.cosmetic_armor"; }
    @Override public boolean hasCustomInventoryName() { return false; }
    @Override public int getInventoryStackLimit() { return 1; }
    @Override public void markDirty() { if (player != null && !player.worldObj.isRemote && AccessoriesPort.NETWORK != null) AccessoriesPort.NETWORK.sendToAll(new SyncCosmeticArmorMessage(player)); }
    @Override public boolean isUseableByPlayer(EntityPlayer who) { return who == player && who.getDistanceSqToEntity(player) < 64.0D; }
    @Override public void openInventory() { }
    @Override public void closeInventory() { }
    @Override public boolean isItemValidForSlot(int slot, ItemStack stack) { return slot >= 0 && slot < 4 && stack != null && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == slot; }
    public void clear() { for (int i = 0; i < 4; i++) stacks[i] = null; markDirty(); }
}
