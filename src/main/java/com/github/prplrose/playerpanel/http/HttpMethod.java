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

    public static HttpMethod getMethod(String requestedMethod) throws HttpException {
        for (HttpMethod method : HttpMethod.values()){
            if(method.name().equals(requestedMethod)){
                return method;
            }
        }
        throw new HttpException(HttpStatusCode.NOT_IMPLEMENTED_ERROR);
    }
}
