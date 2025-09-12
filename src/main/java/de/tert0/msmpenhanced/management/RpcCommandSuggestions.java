package de.tert0.msmpenhanced.management;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record RpcCommandSuggestions(int start, int end, List<RpcCommandSuggestion> suggestions) {
    public static final Codec<RpcCommandSuggestions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT
                    .fieldOf("start")
                    .forGetter(RpcCommandSuggestions::start),
            Codec.INT
                    .fieldOf("end")
                    .forGetter(RpcCommandSuggestions::end),
            RpcCommandSuggestion.CODEC
                    .listOf()
                    .fieldOf("suggestions")
                    .forGetter(RpcCommandSuggestions::suggestions)
    ).apply(instance, RpcCommandSuggestions::new));
}
