package de.tert0.msmpenhanced.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.tert0.msmpenhanced.MsmpEnhancedMod;
import net.minecraft.server.jsonrpc.Connection;
import net.minecraft.server.jsonrpc.JsonRpcLogger;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Connection.class)
public abstract class ConnectionMixin {
    @WrapOperation(method = "channelActive", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/jsonrpc/JsonRpcLogger;log(Lnet/minecraft/server/jsonrpc/methods/ClientInfo;Ljava/lang/String;[Ljava/lang/Object;)V"))
    void modifyLogConnectionOpenMessage(JsonRpcLogger instance, ClientInfo clientInfo, String message, Object[] args, Operation<Void> original) {
        if(MsmpEnhancedMod.getConfig().unixSocketEnabled()) {
            original.call(instance, clientInfo, "Management connection opened", new Object[0]);
        } else {
            original.call(instance, clientInfo, message, args);
        }
    }

    @WrapOperation(method = "channelInactive", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/jsonrpc/JsonRpcLogger;log(Lnet/minecraft/server/jsonrpc/methods/ClientInfo;Ljava/lang/String;[Ljava/lang/Object;)V"))
    void modifyLogConnectionClosedMessage(JsonRpcLogger instance, ClientInfo clientInfo, String message, Object[] args, Operation<Void> original) {
        if(MsmpEnhancedMod.getConfig().unixSocketEnabled()) {
            original.call(instance, clientInfo, "Management connection closed", new Object[0]);
        } else {
            original.call(instance, clientInfo, message, args);
        }
    }
}
