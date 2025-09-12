package de.tert0.msmpenhanced.management;

import de.tert0.msmpenhanced.mixin.RpcSchemaAccessor;
import net.minecraft.server.dedicated.management.schema.RpcSchema;
import net.minecraft.server.dedicated.management.schema.RpcSchemaEntry;

public class ModRpcSchema {
    public static final RpcSchemaEntry COMMAND_SUGGESTION = RpcSchemaAccessor.registerEntry(
            "msmp_enhanced_command_suggestion",
            RpcSchema.ofObject()
                    .withProperty("text", RpcSchema.STRING.toItemSchema())
                    .withProperty("tooltip", RpcSchema.ofObject().toItemSchema()) // TODO optional
    );

    public static final RpcSchemaEntry COMMAND_SUGGESTIONS = RpcSchemaAccessor.registerEntry(
            "msmp_enhanced_command_suggestions",
            RpcSchema.ofObject()
                    .withProperty("start", RpcSchema.INTEGER.toItemSchema())
                    .withProperty("end", RpcSchema.INTEGER.toItemSchema())
                    .withProperty("suggestions", ModRpcSchema.COMMAND_SUGGESTION.ref().toItemSchema())
    );
}