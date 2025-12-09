package de.tert0.msmpenhanced.management;

import de.tert0.msmpenhanced.mixin.RpcSchemaAccessor;
import net.minecraft.server.dedicated.management.schema.RpcSchema;
import net.minecraft.server.dedicated.management.schema.RpcSchemaEntry;
import net.minecraft.text.TextCodecs;

public class ModRpcSchema {
    public static final RpcSchemaEntry<RpcCommandSuggestion> COMMAND_SUGGESTION = RpcSchemaAccessor.registerEntry(
            "msmp_enhanced_command_suggestion",
            RpcSchema.ofObject(RpcCommandSuggestion.CODEC)
                    .withProperty("text", RpcSchema.STRING)
                    .withProperty("tooltip", RpcSchema.ofObject(TextCodecs.CODEC)) // TODO optional
    );

    public static final RpcSchemaEntry<RpcCommandSuggestions> COMMAND_SUGGESTIONS = RpcSchemaAccessor.registerEntry(
            "msmp_enhanced_command_suggestions",
            RpcSchema.ofObject(RpcCommandSuggestions.CODEC)
                    .withProperty("start", RpcSchema.INTEGER)
                    .withProperty("end", RpcSchema.INTEGER)
                    .withProperty("suggestions", ModRpcSchema.COMMAND_SUGGESTION.ref())
    );
}