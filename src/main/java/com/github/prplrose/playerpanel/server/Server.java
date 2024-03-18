package com.github.prplrose.playerpanel.server;

import com.github.prplrose.playerpanel.Config;
import com.github.prplrose.playerpanel.PlayerPanel;
import com.github.prplrose.playerpanel.webpage.EncryptionManager;
import com.github.prplrose.playerpanel.webpage.PageManager;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class Server extends Thread{

    int port;
    Logger LOGGER = PlayerPanel.LOGGER;

    public Server(Path configDirectory) throws IOException {

        LOGGER.info("Initializing encryption");
        EncryptionManager.init(configDirectory);
        LOGGER.info("Initializing config");
        Config.init();
        LOGGER.info("Loading config");
        Config.load(configDirectory);

        if(Config.hostWebpage.get()){
            PageManager.load(configDirectory.resolve("webpage").toFile());
        }

        LOGGER.info("Starting server on port " + Config.port.get());
        this.port = Config.port.get();
        ServerListener listenerThread = new ServerListener(this.port);
        listenerThread.start();
    }

    public static void main(String[] args) throws IOException {
        new Server(Path.of(System.getProperty("user.dir") + "\\config\\PlayerPanel")).start();
    }
}
