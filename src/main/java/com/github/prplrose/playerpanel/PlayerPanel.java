package com.github.prplrose.playerpanel;

import com.github.prplrose.playerpanel.server.Server;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PlayerPanel implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("PlayerPanel");

    @Override
    public void onInitialize() {

        LOGGER.info("Initializing config");
        Config.init();
        try {
            LOGGER.info("Loading config");
            Config.load(FabricLoader.getInstance().getConfigDir());
            LOGGER.info(FabricLoader.getInstance().getConfigDir().toString());
        } catch (IOException | CommandSyntaxException e) {
            LOGGER.error("Was not able to read config :(");
            LOGGER.error(e.toString());
        }

        try {
            new Server().start();

        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }


}
