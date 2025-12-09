package de.tert0.msmpenhanced.mixin;

import net.minecraft.server.dedicated.management.schema.RpcSchema;
import net.minecraft.server.dedicated.management.schema.RpcSchemaEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RpcSchema.class)
public interface RpcSchemaAccessor {
    @Invoker("registerEntry")
    static <T> RpcSchemaEntry<T> registerEntry(String reference, RpcSchema<T> schema) {
        throw new AssertionError("unreachable");
    }
}
