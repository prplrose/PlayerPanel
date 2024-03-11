package com.github.prplrose.playerpanel.http;

public enum HttpMethod {
    GET, HEAD;
    public static final int MAX_LENGTH;

    static {
        int maxLength = -1;
        for(HttpMethod method : values()){
            maxLength = method.name().length();
        }
        MAX_LENGTH = maxLength;
    }
}
