package com.github.prplrose.playerpanel.server;

import com.github.prplrose.playerpanel.PlayerPanel;
import com.github.prplrose.playerpanel.http.HttpStatusCode;
import com.github.prplrose.playerpanel.http.httpmessage.HttpRequest;
import com.github.prplrose.playerpanel.http.httpmessage.HttpResponse;
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

        try (InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {

            new HttpRequest(inputStream);
            HttpResponse httpResponse = HttpResponse.builder(HttpStatusCode.OK).build();
            httpResponse.setBody(html);

            String response = httpResponse.toString();
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
