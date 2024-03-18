package com.github.prplrose.playerpanel.http.httpmessage;

import com.github.prplrose.playerpanel.http.HttpMethod;
import com.github.prplrose.playerpanel.http.HttpException;
import com.github.prplrose.playerpanel.http.HttpStatusCode;
import com.github.prplrose.playerpanel.http.HttpVersion;
import com.github.prplrose.playerpanel.http.httpmessage.headers.HeaderManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest extends HttpMessage {


    public HttpRequest(InputStream inputStream) throws HttpException {
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
                    throw new HttpException(HttpStatusCode.BAD_REQUEST);
                }
                if (this.head == null){
                    if (startLine==null){   // first line is start-line
                        startLine = feed.toString();
                    }
                    else if (feed.length()>0) {
                        headers.add(feed.toString());
                    }else{  // empty line signals the end of body
                        this.head = new RequestHead(startLine, headers.toArray(new String[0]));

                        int bodyLength = this.head.getHeaderManager().getContentLength();
                        feed.delete(0, feed.length());

                        for(int i=0; i<bodyLength; i++){
                            currChar = reader.read();
                            if (currChar==-1){
                                break;
                            }
                            c = (char)currChar;
                            feed.append(c);
                        }
                        this.body = feed.toString();

                        return;
                    }
                    feed.delete(0, feed.length());
                }
            }
        }catch (IOException e){
            throw new HttpException(HttpStatusCode.BAD_REQUEST);
        }
    }

    public RequestHead getRequestHead(){
        return (RequestHead) this.head;
    }


    public static class RequestHead extends AbstractHead {

        private static final Pattern REQUEST_LINE_PATTERN = Pattern.compile("^(?<method>[A-Z]{1,32}) (?<target>\\S*) (?<version>HTTP/\\d\\.\\d)");

        HttpMethod httpMethod;
        URI target;
        HttpVersion httpVersion;
        HeaderManager headerManager;

        public RequestHead(@NotNull String startLine, @Nullable String[] headers) throws HttpException {
            Matcher matcher = REQUEST_LINE_PATTERN.matcher(startLine);
            if(!matcher.find() || matcher.groupCount() != 3){
                throw new HttpException(HttpStatusCode.BAD_REQUEST);
            }
            this.httpMethod = HttpMethod.getMethod(matcher.group("method"));
            this.target = URI.create("/").relativize(URI.create(matcher.group("target")));
            this.httpVersion = HttpVersion.getBestCompatibleVersion(matcher.group("version"));
            headerManager = new HeaderManager(headers);
        }
        public HttpMethod getMethod() {
            return this.httpMethod;
        }
        public URI getTarget() {
            return this.target;
        }

        public HttpVersion getVersion(){
            return this.httpVersion;
        }

        @Override
        public HeaderManager getHeaderManager() {
            return this.headerManager;
        }

        @Override
        public String toString() {
            return String.valueOf(this.httpMethod) +
                    ' ' +
                    this.target +
                    ' ' +
                    this.httpVersion +
                    "\r\n" +
                    this.getHeaderManager().getHeadersAsString();
        }
    }
}