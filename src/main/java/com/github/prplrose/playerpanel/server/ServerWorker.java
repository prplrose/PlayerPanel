package com.github.prplrose.playerpanel.server;

import com.github.prplrose.playerpanel.PlayerPanel;
import com.github.prplrose.playerpanel.http.HttpParser;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerWorker extends Thread{

    Logger LOGGER = PlayerPanel.LOGGER;

    private final Socket socket;

    public ServerWorker(Socket socket){
        this.socket= socket;
    }

    @Override
    public void run(){

        final String html = "<html><head></head><body>Hello world</body></html>";
        final String CRLF = "\n\r";

        try (InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {

            HttpParser.parse(inputStream);

            String response = "HTTP/1.1 200 OK" + CRLF + "Content-Length: " + html.getBytes().length + CRLF + CRLF + html + CRLF + CRLF;
            outputStream.write(response.getBytes());

        } catch (Exception e) {
            LOGGER.error(e.toString());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

}
