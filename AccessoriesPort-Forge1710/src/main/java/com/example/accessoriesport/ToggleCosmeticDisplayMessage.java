package com.example.accessoriesport;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class ToggleCosmeticDisplayMessage implements IMessage {
    private boolean show;
    public ToggleCosmeticDisplayMessage() { }
    public ToggleCosmeticDisplayMessage(boolean show) { this.show = show; }
    public void fromBytes(ByteBuf buf) { show = buf.readBoolean(); }
    public void toBytes(ByteBuf buf) { buf.writeBoolean(show); }
    public static class Handler implements IMessageHandler<ToggleCosmeticDisplayMessage, IMessage> {
        public IMessage onMessage(ToggleCosmeticDisplayMessage message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            CosmeticArmorData data = CosmeticArmorData.get(player);
            if (data != null) data.setShowFunctionalArmorWhenEmpty(message.show);
            return null;
        }
    }
}
