package de.tert0.msmpenhanced.mixin;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.unix.DomainSocketAddress;
import net.minecraft.server.dedicated.management.network.BearerAuthenticationHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BearerAuthenticationHandler.class)
public abstract class BearerAuthenticationHandlerMixin {
    @Inject(method = "getHostAddress", at = @At("HEAD"), cancellable = true)
    void getHostAddress(ChannelHandlerContext channelHandlerContext, CallbackInfoReturnable<String> cir) {
        if(channelHandlerContext.channel().remoteAddress() instanceof DomainSocketAddress) {
            cir.setReturnValue("<UNIX DOMAIN SOCKET>");
        }
    }
}
