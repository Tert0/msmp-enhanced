package de.tert0.msmpenhanced.jsonrpc;

import de.tert0.msmpenhanced.mixin.SchemaAccessor;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.jsonrpc.api.Schema;
import net.minecraft.server.jsonrpc.api.SchemaComponent;
import org.jetbrains.annotations.NotNull;

public class ModSchema {
    public static final SchemaComponent<@NotNull RpcCommandSuggestion> COMMAND_SUGGESTION = SchemaAccessor.registerSchema(
            "msmp_enhanced_command_suggestion",
            Schema.record(RpcCommandSuggestion.CODEC)
                    .withField("text", Schema.STRING_SCHEMA)
                    .withField("tooltip", Schema.record(ComponentSerialization.CODEC)) // TODO optional
    );

    public static final SchemaComponent<@NotNull RpcCommandSuggestions> COMMAND_SUGGESTIONS = SchemaAccessor.registerSchema(
            "msmp_enhanced_command_suggestions",
            Schema.record(RpcCommandSuggestions.CODEC)
                    .withField("start", Schema.INT_SCHEMA)
                    .withField("end", Schema.INT_SCHEMA)
                    .withField("suggestions", ModSchema.COMMAND_SUGGESTION.asRef())
    );
}