package com.github.prplrose.playerpanel.http;

import com.github.prplrose.playerpanel.http.httpmessage.Head;
import com.github.prplrose.playerpanel.http.httpmessage.HttpMessage;
import com.github.prplrose.playerpanel.http.httpmessage.headers.HeaderManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest extends HttpMessage {

    RequestHead head;

    public HttpRequest(InputStream inputStream) throws HttpParsingException {
        super(inputStream);
    }

    public HttpMethod getMethod() {
        return head.httpMethod;
    }
    public String getTarget() {
        return head.target;
    }

    public HttpVersion getCompatibleVersion(){
        return head.httpVersion;
    }

    @Override
    public Head constructHead(@NotNull String startLine, @Nullable String[] headers) throws HttpParsingException {
        RequestHead requestHead = new RequestHead(startLine, headers);
        this.head = requestHead;
        return requestHead;
    }


    static class RequestHead implements Head {

        private static final Pattern REQUEST_LINE_PATTERN = Pattern.compile("^(?<method>[A-Z]{1,32}) (?<target>\\S*) (?<version>HTTP/\\d\\.\\d)");

        HttpMethod httpMethod;
        String target;
        HttpVersion httpVersion;
        HeaderManager headerManager;

        public RequestHead(@NotNull String startLine, @Nullable String[] headers) throws HttpParsingException{
            Matcher matcher = REQUEST_LINE_PATTERN.matcher(startLine);
            if(!matcher.find() || matcher.groupCount() != 3){
                throw new HttpParsingException(HttpStatusCode.BAD_REQUEST);
            }
            this.httpMethod = HttpMethod.getMethod(matcher.group("method"));
            this.target = matcher.group("target");
            this.httpVersion = HttpVersion.getBestCompatibleVersion(matcher.group("version"));
            headerManager = new HeaderManager(headers);
        }


        @Override
        public HeaderManager getHeaderManager() {
            return this.headerManager;
        }
    }
}