package de.tert0.msmpenhanced.management;

import de.tert0.msmpenhanced.MsmpEnhancedMod;
import de.tert0.msmpenhanced.management.handler.CommandRpcHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.dedicated.management.IncomingRpcMethod;
import net.minecraft.server.dedicated.management.schema.RpcSchema;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

public class ModIncomingRpcMethods {
    private static <P, R> void register(String name, IncomingRpcMethod.Builder<P, R> methodBuilder) {
        Registry.register(
                Registries.INCOMING_RPC_METHOD, Identifier.of(MsmpEnhancedMod.MOD_ID, name),
                methodBuilder.build()
        );
    }

    public static void initialize() {
        register(
                "command/run",
                IncomingRpcMethod.createParameterizedBuilder(CommandRpcHandler::run)
                        .description("Run a console command")
                        .parameter("command", RpcSchema.STRING)
                        .result("messages", RpcSchema.ofObject(TextCodecs.CODEC).asArray()) // TODO schema
        );
        register(
                "command/suggest",
                IncomingRpcMethod.createParameterizedBuilder(CommandRpcHandler::suggest)
                        .description("Suggest command completions")
                        .parameter("partialCommand", RpcSchema.STRING)
                        .result("suggestions", ModRpcSchema.COMMAND_SUGGESTIONS.ref())
        );
    }
}
