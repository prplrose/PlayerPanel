package com.github.prplrose.playerpanel.http.httpmessage;

import com.github.prplrose.playerpanel.http.HttpStatusCode;
import com.github.prplrose.playerpanel.http.HttpVersion;
import com.github.prplrose.playerpanel.http.httpmessage.headers.HeaderManager;

public class HttpResponse extends HttpMessage{

    private ResponseHead head;
    private String body;

    private HttpResponse(ResponseHead head){
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        int contentLength = this.body.getBytes().length;
        if (contentLength > 0){
            this.head.getHeaderManager().setContentLength(contentLength);
        }
    }

    public static Builder builder(HttpStatusCode statusCode){
        return new Builder(statusCode);
    }

    public byte[] toByteArray(){
        return this.toString().getBytes();
    }

    @Override
    public String toString() {
        return this.head.toString() + this.body;
    }

    static class ResponseHead extends AbstractHead{

        HeaderManager headerManager;
        HttpStatusCode statusCode;

        public ResponseHead(HttpStatusCode statusCode, String[] headers){
            this.statusCode = statusCode;
            this.headerManager = new HeaderManager(headers);
        }

        public ResponseHead(HttpStatusCode statusCode){
            this.statusCode = statusCode;
            this.headerManager = new HeaderManager();
        }

        @Override
        public HeaderManager getHeaderManager() {
            return this.headerManager;
        }

        @Override
        public String toString() {
            return HttpVersion.HTTP_1_1.LITERAL +
                    ' ' +
                    this.statusCode.STATUS_CODE +
                    ' ' +
                    this.statusCode.MESSAGE +
                    "\r\n" +
                    this.getHeaderManager().getHeadersAsString();
        }
    }

    public static class Builder{
        HttpStatusCode statusCode;

        Builder(HttpStatusCode statusCode) {
            this.statusCode = statusCode;
        }

        public HttpResponse build(){
            ResponseHead head = new ResponseHead(statusCode);
            HttpResponse response = new HttpResponse(head);
            response.setBody("");
            return response;
        }


    }
}
