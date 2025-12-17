package de.tert0.msmpenhanced.jsonrpc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public record RpcCommandSuggestion(String text, Optional<Component> tooltip) {
    public static final Codec<RpcCommandSuggestion> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING
                    .fieldOf("text")
                    .forGetter(RpcCommandSuggestion::text),
            ComponentSerialization.CODEC
                    .optionalFieldOf("tooltip")
                    .forGetter(RpcCommandSuggestion::tooltip)
    ).apply(instance, RpcCommandSuggestion::new));
}
