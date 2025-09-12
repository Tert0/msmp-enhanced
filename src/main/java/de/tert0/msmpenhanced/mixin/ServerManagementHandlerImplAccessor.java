package de.tert0.msmpenhanced.mixin;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.management.handler.ServerManagementHandlerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerManagementHandlerImpl.class)
public interface ServerManagementHandlerImplAccessor {
    @Accessor("server")
    MinecraftDedicatedServer getServer();
}
