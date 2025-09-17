package de.tert0.msmpenhanced.management;

import com.mojang.serialization.Codec;
import de.tert0.msmpenhanced.MsmpEnhancedMod;
import de.tert0.msmpenhanced.management.handler.CommandRpcHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.dedicated.management.IncomingRpcMethod;
import net.minecraft.server.dedicated.management.RpcRequestParameter;
import net.minecraft.server.dedicated.management.RpcResponseResult;
import net.minecraft.server.dedicated.management.schema.RpcSchema;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

public class ModIncomingRpcMethods {
    private static <T extends IncomingRpcMethod> void register(String name, IncomingRpcMethod.Builder<T> methodBuilder) {
        Registry.register(
                Registries.INCOMING_RPC_METHOD, Identifier.of(MsmpEnhancedMod.MOD_ID, name),
                methodBuilder.build()
        );
    }

    public static void initialize() {
        register(
                "command/run",
                IncomingRpcMethod.createParameterizedBuilder(CommandRpcHandler::run, Codec.STRING, TextCodecs.CODEC.listOf())
                        .description("Run a console command")
                        .parameter(new RpcRequestParameter("command", RpcSchema.STRING))
                        .result(new RpcResponseResult("messages", RpcSchema.ofObject().asArray())) // TODO schema
        );
        register(
                "command/suggest",
                IncomingRpcMethod.createParameterizedBuilder(CommandRpcHandler::suggest, Codec.STRING, RpcCommandSuggestions.CODEC)
                        .description("Suggest command completions")
                        .parameter(new RpcRequestParameter("partialCommand", RpcSchema.STRING))
                        .result(new RpcResponseResult("suggestions", ModRpcSchema.COMMAND_SUGGESTIONS.ref()))
        );
    }
}
