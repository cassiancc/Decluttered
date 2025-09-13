package cc.cassian.decluttered;

import cc.cassian.decluttered.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Decluttered implements ModInitializer {

    public static final String MOD_ID = "decluttered";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final ModConfig CONFIG = ModConfig.createToml(FabricLoader.getInstance().getConfigDir(), "" , MOD_ID, ModConfig.class);

    @Override
    public void onInitialize() {

    }
}
