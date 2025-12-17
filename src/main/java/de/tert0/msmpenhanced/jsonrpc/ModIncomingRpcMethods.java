package de.tert0.msmpenhanced.jsonrpc;

import de.tert0.msmpenhanced.MsmpEnhancedMod;
import de.tert0.msmpenhanced.jsonrpc.methods.CommandRpcService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.server.jsonrpc.IncomingRpcMethod;
import net.minecraft.server.jsonrpc.api.Schema;
import org.jetbrains.annotations.NotNull;

public class ModIncomingRpcMethods {
    private static <P, R> void register(String name, IncomingRpcMethod.IncomingRpcMethodBuilder<@NotNull P, @NotNull R> methodBuilder) {
        Registry.register(
                BuiltInRegistries.INCOMING_RPC_METHOD, Identifier.fromNamespaceAndPath(MsmpEnhancedMod.MOD_ID, name),
                methodBuilder.build()
        );
    }

    public static void initialize() {
        register(
                "command/run",
                IncomingRpcMethod.method(CommandRpcService::run)
                        .description("Run a console command")
                        .param("command", Schema.STRING_SCHEMA)
                        .response("messages", Schema.record(ComponentSerialization.CODEC).asArray()) // TODO schema
        );
        register(
                "command/suggest",
                IncomingRpcMethod.method(CommandRpcService::suggest)
                        .description("Suggest command completions")
                        .param("partialCommand", Schema.STRING_SCHEMA)
                        .response("suggestions", ModSchema.COMMAND_SUGGESTIONS.asRef())
        );
    }
}
