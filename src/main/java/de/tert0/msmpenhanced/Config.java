package de.tert0.msmpenhanced;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StrictJsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public record Config(boolean unixSocketEnabled, @NotNull String unixSocketPath) {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL
                    .fieldOf("unixSocketEnabled")
                    .forGetter(Config::unixSocketEnabled),
            Codec.STRING
                    .fieldOf("unixSocketPath")
                    .forGetter(Config::unixSocketPath)
    ).apply(instance, Config::new));

    private static final Config DEFAULT = new Config(false, "msmp.sock");

    public static Config load(Path configPath) {
        File file = configPath.toFile();

        Config config = Config.DEFAULT;
        if(file.exists()) {
            try {
                DataResult<Config> result = Config.CODEC.parse(
                        JsonOps.INSTANCE,
                        StrictJsonParser.parse(Files.readString(configPath))
                );
                config = result.resultOrPartial(MsmpEnhancedMod.LOGGER::error).orElseThrow();
            } catch (IOException e) {
                MsmpEnhancedMod.LOGGER.error("Failed to load config file", e);
            }
        } else if(file.getParentFile().canWrite() || file.canWrite()) {
            DataResult<JsonElement> result = Config.CODEC.encodeStart(JsonOps.INSTANCE, Config.DEFAULT);
            JsonElement json = result.getOrThrow();
            try(Writer writer = new FileWriter(file)) {
                GSON.toJson(json, GSON.newJsonWriter(writer));
            } catch (IOException e) {
                MsmpEnhancedMod.LOGGER.error("Failed to write default config file", e);
            }
        } else {
            MsmpEnhancedMod.LOGGER.info("Config at {} is not writable. Using default config.", configPath);
        }

        if(config.unixSocketEnabled && config.unixSocketPath.isEmpty()) {
            throw new IllegalArgumentException("Empty unix socket path");
        }

        return config;
    }
}
