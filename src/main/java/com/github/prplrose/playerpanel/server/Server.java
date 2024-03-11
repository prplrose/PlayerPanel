package com.github.prplrose.playerpanel.server;

import com.github.prplrose.playerpanel.Config;
import com.github.prplrose.playerpanel.PlayerPanel;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class Server extends Thread{

    int port;
    Logger LOGGER = PlayerPanel.LOGGER;

    public Server() throws IOException {
        LOGGER.info("Starting server on port " + Config.port.get());
        this.port = Config.port.get();
        ServerListener listenerThread = new ServerListener(this.port);
        listenerThread.start();
    }

    public static void main(String[] args) throws IOException, CommandSyntaxException {
        Config.init();
        Config.load(Path.of(System.getProperty("user.dir") + "\\config"));
        new Server().start();
    }
}
