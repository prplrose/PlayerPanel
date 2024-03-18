package com.github.prplrose.playerpanel.server;

import com.github.prplrose.playerpanel.PlayerPanel;
import com.github.prplrose.playerpanel.http.HttpException;
import com.github.prplrose.playerpanel.http.HttpStatusCode;
import com.github.prplrose.playerpanel.http.httpmessage.HttpRequest;
import com.github.prplrose.playerpanel.http.httpmessage.HttpResponse;
import com.github.prplrose.playerpanel.webpage.PageManager;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;

public class ServerWorker extends Thread{

    Logger LOGGER = PlayerPanel.LOGGER;

    private final Socket socket;

    public ServerWorker(Socket socket){
        this.socket= socket;
    }

    @Override
    public void run(){

        try (InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {

            HttpRequest request = new HttpRequest(inputStream);
            HttpResponse response;
            URI target = URI.create(request.getRequestHead().getTarget().getPath());
            response = HttpResponse.builder(HttpStatusCode.OK).build();
            try{
                response.setBody(PageManager.getPage(target));
            }catch (HttpException e){
                response = HttpResponse.builder(e.getErrorCode()).build();
            }
            outputStream.write(response.toByteArray());

            LOGGER.info(request.getRequestHead().toString());


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
