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

    public static HttpMethod getMethod(String requestedMethod) throws HttpParsingException {
        for (HttpMethod method : HttpMethod.values()){
            if(method.name().equals(requestedMethod)){
                return method;
            }
        }
        throw new HttpParsingException(HttpStatusCode.NOT_IMPLEMENTED_ERROR);
    }
}
