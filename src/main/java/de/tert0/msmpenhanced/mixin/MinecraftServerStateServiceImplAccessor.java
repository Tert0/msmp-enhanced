package de.tert0.msmpenhanced.mixin;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.jsonrpc.internalapi.MinecraftServerStateServiceImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftServerStateServiceImpl.class)
public interface MinecraftServerStateServiceImplAccessor {
    @Invoker("server")
    DedicatedServer msmpenhanced$server();
}
