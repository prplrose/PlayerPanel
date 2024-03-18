package com.github.prplrose.playerpanel;

import com.github.prplrose.playerpanel.server.Server;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PlayerPanel implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("PlayerPanel");

    @Override
    public void onInitialize() {
        try {
            new Server(FabricLoader.getInstance().getConfigDir()).start();

        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }


}
