package com.example.accessoriesport;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class OpenCosmeticGuiMessage implements IMessage {
    public void fromBytes(ByteBuf buf) { }
    public void toBytes(ByteBuf buf) { }
    public static class Handler implements IMessageHandler<OpenCosmeticGuiMessage, IMessage> {
        public IMessage onMessage(OpenCosmeticGuiMessage message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            player.openGui(AccessoriesPortHolder.INSTANCE, 0, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
            return null;
        }
    }
}
