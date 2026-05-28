package de.tert0.msmpenhanced.jsonrpc.methods;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import de.tert0.msmpenhanced.jsonrpc.RpcCommandSuggestion;
import de.tert0.msmpenhanced.jsonrpc.RpcCommandSuggestions;
import de.tert0.msmpenhanced.mixin.MinecraftServerStateServiceImplAccessor;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandRpcService {
    private static CommandSourceStack createCommandSourceStack(MinecraftServer server, CommandSource commandSource, ClientInfo clientInfo) {
        ServerLevel serverWorld = server.getLevel(server.getRespawnData().dimension());
        String name = "RPC Connection #" + clientInfo.connectionId();
        return new CommandSourceStack(
                commandSource, Vec3.atLowerCornerOf(server.getRespawnData().pos()), Vec2.ZERO, serverWorld,
                LevelBasedPermissionSet.OWNER, name, Component.literal(name), server, null
        );
    }

    private static Component addTranslationFallback(Component text) {
        ComponentContents textContent = text.getContents();
        if(textContent instanceof TranslatableContents translatable && translatable.getFallback() == null) {
            textContent = new TranslatableContents(
                    translatable.getKey(),
                    Language.getInstance().getOrDefault(translatable.getKey(), null),
                    translatable.getArgs()
            );
        }
        MutableComponent result = MutableComponent.create(textContent);
        result.setStyle(text.getStyle());
        for(Component sibling : text.getSiblings()) {
            result.append(addTranslationFallback(sibling));
        }
        return result;
    }

    public static List<Component> run(MinecraftApi minecraftApi, String command, ClientInfo clientInfo) {
        DedicatedServer server = ((MinecraftServerStateServiceImplAccessor) minecraftApi.serverStateService()).msmpenhanced$server();

        final List<Component> messages = new ArrayList<>();
        CommandSource commandSource = new CommandSource() {
            @Override
            public void sendSystemMessage(@NotNull Component message) {
                messages.add(addTranslationFallback(message));
            }

            @Override
            public boolean acceptsSuccess() {
                return true;
            }

            @Override
            public boolean acceptsFailure() {
                return true;
            }

            @Override
            public boolean shouldInformAdmins() {
                return server.shouldInformAdmins();
            }
        };

        CommandSourceStack commandSourceStack = createCommandSourceStack(server, commandSource, clientInfo);
        server.getCommands().performPrefixedCommand(commandSourceStack, command);

        return messages;
    }

    public static RpcCommandSuggestions suggest(MinecraftApi minecraftApi, String partialCommand, ClientInfo clientInfo) {
        DedicatedServer server = ((MinecraftServerStateServiceImplAccessor) minecraftApi.serverStateService()).msmpenhanced$server();
        CommandSourceStack commandSourceStack = createCommandSourceStack(server, CommandSource.NULL, clientInfo);

        ParseResults<CommandSourceStack> parseResults = server.getCommands().getDispatcher().parse(new StringReader(partialCommand), commandSourceStack);
        Suggestions suggestions = server.getCommands().getDispatcher().getCompletionSuggestions(parseResults).join();

        return new RpcCommandSuggestions(
                suggestions.getRange().getStart(),
                suggestions.getRange().getEnd(),
                suggestions.getList()
                        .stream()
                        .map(suggestion -> new RpcCommandSuggestion(
                                suggestion.getText(),
                                Optional.ofNullable(suggestion.getTooltip())
                                        .map(ComponentUtils::fromMessage)
                        ))
                        .toList()
        );
    }
}
