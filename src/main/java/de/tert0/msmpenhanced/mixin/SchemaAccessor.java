package de.tert0.msmpenhanced.mixin;

import net.minecraft.server.jsonrpc.api.Schema;
import net.minecraft.server.jsonrpc.api.SchemaComponent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Schema.class)
public interface SchemaAccessor {
    @Invoker("registerSchema")
    static <T> SchemaComponent<@NotNull T> registerSchema(String reference, Schema<@NotNull T> schema) {
        throw new AssertionError("unreachable");
    }
}
