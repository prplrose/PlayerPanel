package com.github.prplrose.playerpanel.server;

import com.github.prplrose.playerpanel.PlayerPanel;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread {

    Logger LOGGER = PlayerPanel.LOGGER;
    private final ServerSocket serverSocket;

    public ServerListener(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                PlayerPanel.LOGGER.info("Accepted connection: " + socket.getInetAddress());
                ServerWorker serverWorker = new ServerWorker(socket);
                serverWorker.start();
            }
        } catch (IOException e) {
            LOGGER.error(e.toString());
        } finally {
            if (serverSocket !=null){
                try {
                    serverSocket.close();
                } catch (IOException ignored) {}
            }
        }
    }
}
