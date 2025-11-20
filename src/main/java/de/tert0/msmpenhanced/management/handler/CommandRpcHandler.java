package de.tert0.msmpenhanced.management.handler;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import de.tert0.msmpenhanced.management.RpcCommandSuggestion;
import de.tert0.msmpenhanced.management.RpcCommandSuggestions;
import de.tert0.msmpenhanced.mixin.ServerManagementHandlerImplAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.management.dispatch.ManagementHandlerDispatcher;
import net.minecraft.server.dedicated.management.network.ManagementConnectionId;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Language;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandRpcHandler {
    private static ServerCommandSource createCommandSource(MinecraftServer server, CommandOutput commandOutput, ManagementConnectionId remote) {
        ServerWorld serverWorld = server.getWorld(server.getSpawnPos().dimension());
        String name = "RPC Connection #" + remote.connectionId();
        return new ServerCommandSource(
                commandOutput, Vec3d.of(server.getSpawnPos().pos()), Vec2f.ZERO, serverWorld,
                4, name, Text.literal(name), server, null
        );
    }

    private static Text addTranslationFallback(Text text) {
        TextContent textContent = text.getContent();
        if(textContent instanceof TranslatableTextContent translatable && translatable.getFallback() == null) {
            textContent = new TranslatableTextContent(
                    translatable.getKey(),
                    Language.getInstance().get(translatable.getKey(), null),
                    translatable.getArgs()
            );
        }
        MutableText result = MutableText.of(textContent);
        result.setStyle(text.getStyle());
        for(Text sibling : text.getSiblings()) {
            result.append(addTranslationFallback(sibling));
        }
        return result;
    }

    public static List<Text> run(ManagementHandlerDispatcher dispatcher, String command, ManagementConnectionId remote) {
        MinecraftDedicatedServer server = ((ServerManagementHandlerImplAccessor) dispatcher.getServerHandler()).getServer();

        final List<Text> messages = new ArrayList<>();
        CommandOutput commandOutput = new CommandOutput() {
            @Override
            public void sendMessage(Text message) {
                messages.add(addTranslationFallback(message));
            }

            @Override
            public boolean shouldReceiveFeedback() {
                return true;
            }

            @Override
            public boolean shouldTrackOutput() {
                return true;
            }

            @Override
            public boolean shouldBroadcastConsoleToOps() {
                return server.shouldBroadcastConsoleToOps();
            }
        };

        ServerCommandSource commandSource = createCommandSource(server, commandOutput, remote);
        server.getCommandManager().parseAndExecute(commandSource, command);

        return messages;
    }

    public static RpcCommandSuggestions suggest(ManagementHandlerDispatcher dispatcher, String partialCommand, ManagementConnectionId remote) {
        MinecraftDedicatedServer server = ((ServerManagementHandlerImplAccessor) dispatcher.getServerHandler()).getServer();
        ServerCommandSource commandSource = createCommandSource(server, CommandOutput.DUMMY, remote);

        ParseResults<ServerCommandSource> parseResults = server.getCommandManager().getDispatcher().parse(new StringReader(partialCommand), commandSource);
        Suggestions suggestions = server.getCommandManager().getDispatcher().getCompletionSuggestions(parseResults).join();

        return new RpcCommandSuggestions(
                suggestions.getRange().getStart(),
                suggestions.getRange().getEnd(),
                suggestions.getList()
                        .stream()
                        .map(suggestion -> new RpcCommandSuggestion(
                                suggestion.getText(),
                                Optional.ofNullable(suggestion.getTooltip())
                                        .map(Texts::toText)
                        ))
                        .toList()
        );
    }
}
