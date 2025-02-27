package com.yyon.grapplinghook.network;

import com.yyon.grapplinghook.client.ClientProxyInterface;
import com.yyon.grapplinghook.grapplemod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.LogicalSide;

public abstract class BaseMessageClient {
	public BaseMessageClient(FriendlyByteBuf buf) {
		this.decode(buf);
	}
	
	public BaseMessageClient() {
	}
	
	public abstract void decode(FriendlyByteBuf buf);
	
	public abstract void encode(FriendlyByteBuf buf);

    @OnlyIn(Dist.CLIENT)
    public abstract void processMessage(CustomPayloadEvent.Context ctx);
    
    public void onMessageReceived(CustomPayloadEvent.Context ctx) {
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        if (sideReceived != LogicalSide.CLIENT) {
			grapplemod.LOGGER.warn("message received on wrong side:" + ctx.getDirection().getReceptionSide());
			return;
        }
        
        ctx.setPacketHandled(true);
        
        ctx.enqueueWork(() -> 
        	ClientProxyInterface.proxy.onMessageReceivedClient(this, ctx)
        );
    }

}
