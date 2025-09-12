package de.tert0.msmpenhanced.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.tert0.msmpenhanced.MsmpEnhancedMod;
import net.minecraft.server.dedicated.management.ManagementLogger;
import net.minecraft.server.dedicated.management.network.ManagementConnectionHandler;
import net.minecraft.server.dedicated.management.network.ManagementConnectionId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ManagementConnectionHandler.class)
public abstract class ManagementConnectionHandlerMixin {
    @WrapOperation(method = "channelActive", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/management/ManagementLogger;logAction(Lnet/minecraft/server/dedicated/management/network/ManagementConnectionId;Ljava/lang/String;[Ljava/lang/Object;)V"))
    void modifyLogConnectionOpenMessage(ManagementLogger instance, ManagementConnectionId remote, String action, Object[] arguments, Operation<Void> original) {
        if(MsmpEnhancedMod.getConfig().unixSocketEnabled()) {
            original.call(instance, remote, "Management connection opened", new Object[0]);
        } else {
            original.call(instance, remote, action, arguments);
        }
    }

    @WrapOperation(method = "channelInactive", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/management/ManagementLogger;logAction(Lnet/minecraft/server/dedicated/management/network/ManagementConnectionId;Ljava/lang/String;[Ljava/lang/Object;)V"))
    void modifyLogConnectionClosedMessage(ManagementLogger instance, ManagementConnectionId remote, String action, Object[] arguments, Operation<Void> original) {
        if(MsmpEnhancedMod.getConfig().unixSocketEnabled()) {
            original.call(instance, remote, "Management connection closed", new Object[0]);
        } else {
            original.call(instance, remote, action, arguments);
        }
    }
}
