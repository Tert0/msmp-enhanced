package de.tert0.msmpenhanced;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class MsmpEnhancedMod implements ModInitializer {
    public static final String MOD_ID = "msmpenhanced";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static Config config;

    @Override
    public void onInitialize() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("msmpenhanced.json");
        MsmpEnhancedMod.config = Config.load(configPath);
    }

    public static Config getConfig() {
        return config;
    }
}
