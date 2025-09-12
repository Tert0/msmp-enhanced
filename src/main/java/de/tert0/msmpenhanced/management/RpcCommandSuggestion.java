package de.tert0.msmpenhanced.management;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

import java.util.Optional;

public record RpcCommandSuggestion(String text, Optional<Text> tooltip) {
    public static final Codec<RpcCommandSuggestion> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING
                    .fieldOf("text")
                    .forGetter(RpcCommandSuggestion::text),
            TextCodecs.CODEC
                    .optionalFieldOf("tooltip")
                    .forGetter(RpcCommandSuggestion::tooltip)
    ).apply(instance, RpcCommandSuggestion::new));
}
