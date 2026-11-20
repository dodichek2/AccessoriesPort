package com.example.accessoriesport;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SyncCosmeticArmorMessage implements IMessage {
    private int entityId;
    private NBTTagCompound data;

    public SyncCosmeticArmorMessage() { }
    public SyncCosmeticArmorMessage(EntityPlayer player) {
        this.entityId = player.getEntityId();
        this.data = writeData(CosmeticArmorData.get(player));
    }
    public void fromBytes(ByteBuf buf) { entityId = buf.readInt(); data = ByteBufUtils.readTag(buf); }
    public void toBytes(ByteBuf buf) { buf.writeInt(entityId); ByteBufUtils.writeTag(buf, data == null ? new NBTTagCompound() : data); }

    private static NBTTagCompound writeData(CosmeticArmorData inventory) {
        NBTTagCompound tag = new NBTTagCompound();
        if (inventory == null) return tag;
        tag.setBoolean("ShowFunctionalWhenEmpty", inventory.shouldShowFunctionalArmorWhenEmpty());
        for (int i = 0; i < 4; i++) {
            ItemStack stack = inventory.getCosmetic(i);
            if (stack != null) tag.setTag("Slot" + i, stack.writeToNBT(new NBTTagCompound()));
        }
        return tag;
    }

    @SideOnly(Side.CLIENT)
    public static class Handler implements IMessageHandler<SyncCosmeticArmorMessage, IMessage> {
        public IMessage onMessage(final SyncCosmeticArmorMessage message, MessageContext ctx) {
            final EntityPlayer player = AccessoriesPortClientProxy.getClientPlayer();
            if (player != null && player.worldObj != null) {
                Entity entity = player.worldObj.getEntityByID(message.entityId);
                if (entity instanceof EntityPlayer) {
                    CosmeticArmorData inventory = CosmeticArmorData.get(entity);
                    if (inventory != null) {
                        for (int i = 0; i < 4; i++) {
                            String key = "Slot" + i;
                            inventory.setInventorySlotContents(i, message.data.hasKey(key) ? ItemStack.loadItemStackFromNBT(message.data.getCompoundTag(key)) : null);
                        }
                        inventory.setShowFunctionalArmorWhenEmpty(message.data.getBoolean("ShowFunctionalWhenEmpty"));
                    }
                }
            }
            return null;
        }
    }
}
