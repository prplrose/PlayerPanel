package com.github.prplrose.playerpanel.http.httpmessage.headers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeaderManager {

    List<AbstractHeader<?>> headers = new ArrayList<>();

    public HeaderManager(String[] headers) {
        final Pattern HEADER_LINE_PATTER = Pattern.compile("(?<name>[\\w-]*):(?<value>.*)");
        Matcher matcher;
        for (String header : headers) {
            if (header == null)
                continue;
            matcher = HEADER_LINE_PATTER.matcher(header);
            if (!matcher.find() || matcher.groupCount() != 2) {
                continue;
            }
            String name = matcher.group("name");
            String value = matcher.group("value");

            if (name.equalsIgnoreCase(Headers.ContentLength.name)){
                addHeader(new Headers.ContentLength(value));
            }
        }
    }

    public HeaderManager(){}

    AbstractHeader<?> getValueOf(String name){
        for( AbstractHeader<?> header : headers){
            if(header.equals(name)){
                return header;
            }
        }
        return null;
    }

    void addHeader(AbstractHeader<?> header){
        if (headers.contains(header)){
            return;
        }
        this.headers.add(header);
    }

    public int getContentLength(){
        Headers.ContentLength contentLengthHeader = (Headers.ContentLength) this.getValueOf(Headers.ContentLength.name);
        if (contentLengthHeader!=null){
            return contentLengthHeader.getValue();
        }
        return 0;
    }

    public void setContentLength(int length){
        this.addHeader(new Headers.ContentLength(length));
    }

    public String getHeadersAsString(){
        StringBuilder stringBuilder = new StringBuilder();

        for(AbstractHeader<?> header : headers){
            stringBuilder.append(header.toString());
        }
        stringBuilder.append("\r\n");

        return stringBuilder.toString();
    }

}
