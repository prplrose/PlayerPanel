package com.github.prplrose.playerpanel.http;

import com.github.prplrose.playerpanel.PlayerPanel;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {

    private final static Logger LOGGER = PlayerPanel.LOGGER;

    private static final int SP = 32;
    private static final int CR = 13;
    private static final int LF = 10;

    public static HttpRequest parse(InputStream inputStream) throws HttpParsingException {
        try {
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
            HttpRequest request = new HttpRequest();
            parseRequestLine(reader, request);
            return request;

        }catch (IOException e){
            LOGGER.info(e.toString());
        }
        return null;
    }

    private static void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder stringBuilder = new StringBuilder();

        boolean methodParsed = false;
        boolean requestTargetParsed = false;

        int _byte;
        while ((_byte = reader.read())>=0){
            if(_byte == CR){
                _byte = reader.read();
                if(_byte==LF){
                    if(!methodParsed || !requestTargetParsed){
                        throw new HttpParsingException(HttpStatusCode.BAD_REQUEST);
                    }
                    try {
                        request.setRequestedVersion(stringBuilder.toString());
                    }catch (BadHttpVersionException e){
                        throw new HttpParsingException(HttpStatusCode.BAD_REQUEST);
                    }
                    return;
                }else {
                    throw new HttpParsingException(HttpStatusCode.BAD_REQUEST);
                }
            }
            if(_byte==SP){
                if(!methodParsed){
                    request.setMethod(stringBuilder.toString());
                    stringBuilder.delete(0, stringBuilder.length());
                    methodParsed = true;
                }else if (!requestTargetParsed){
                    request.setTarget(stringBuilder.toString());
                    stringBuilder.delete(0, stringBuilder.length());
                    requestTargetParsed = true;
                }else {
                    throw new HttpParsingException(HttpStatusCode.BAD_REQUEST);
                }
            }else {
                stringBuilder.append((char)_byte);
                if(!methodParsed){
                    if(stringBuilder.length() > HttpMethod.MAX_LENGTH){
                        throw new HttpParsingException(HttpStatusCode.NOT_IMPLEMENTED_ERROR);
                    }
                }
            }
        }
    }

    private static void parseHeaders(InputStreamReader reader, HttpRequest request){

    }

    private static void parseBody(InputStreamReader reader, HttpRequest request){

    }

}
