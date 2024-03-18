package com.github.prplrose.playerpanel.http;

public class HttpException extends Exception{
    private final HttpStatusCode errorCode;

    public HttpException(HttpStatusCode errorCode){
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public HttpStatusCode getErrorCode(){
        return  errorCode;
    }


}
