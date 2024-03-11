package com.github.prplrose.playerpanel.http.httpmessage;

import com.github.prplrose.playerpanel.http.HttpParsingException;
import com.github.prplrose.playerpanel.http.HttpStatusCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class HttpMessage {

    Head head = null;
    public String body;

    public HttpMessage(InputStream inputStream) throws HttpParsingException{
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        StringBuilder feed = new StringBuilder();
        List<String> headers = new ArrayList<>();
        String startLine = null;
        int currChar;
        try{
            while ((currChar = reader.read())>0){
                char c = (char)currChar;
                if(c != '\r'){
                    feed.append(c);   // default case when read character is neither CR nor LF
                    continue;
                }
                currChar = reader.read();
                c = (char)currChar;
                if(c!='\n'){  // check if LF is after CR, if not throw exception
                    throw new HttpParsingException(HttpStatusCode.BAD_REQUEST);
                }
                if (this.head == null){
                    if (startLine==null){   // first line is start-line
                        startLine = feed.toString();
                    }
                    else if (feed.length()>0) {
                        headers.add(feed.toString());
                    }else{  // empty line signals the end of body
                        this.head = constructHead(startLine, headers.toArray(new String[0]));
                        readBody(reader, this.head.getHeaderManager().getContentLength());
                        return;
                    }
                    feed.delete(0, feed.length());
                }
            }
        }catch (IOException e){
            throw new HttpParsingException(HttpStatusCode.BAD_REQUEST);
        }
    }

    void readBody(InputStreamReader reader, int length) throws IOException {
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<length; i++){
            int currChar = reader.read();
            if (currChar==-1){
                break;
            }
            char c = (char)currChar;
            builder.append(c);
        }
        this.body = builder.toString();
    }

    protected abstract Head constructHead(@NotNull String startLine, @Nullable String[] headers) throws HttpParsingException;

}
