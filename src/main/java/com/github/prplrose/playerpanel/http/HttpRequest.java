package com.github.prplrose.playerpanel.http;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private String target;
    //private String requestedVersion;
    private HttpVersion bestCompatibleVersion;

    HttpRequest() {

    }

    public HttpMethod getMethod() {
        return method;
    }

    void setMethod(String methodName) throws HttpParsingException {
        for(HttpMethod method: HttpMethod.values()){
            if(methodName.equals(method.name())){
                this.method = method;
                return;
            }
        }
        throw new HttpParsingException(
                HttpStatusCode.NOT_IMPLEMENTED_ERROR
        );
    }

    public String getTarget() {
        return this.target;
    }

    void setTarget(String target) throws HttpParsingException {
        if(target==null || target.length()==0){
            throw new HttpParsingException(HttpStatusCode.INTERNAL_SERVER_ERROR);
        }
        this.target = target;
    }


    public HttpVersion getCompatibleVersion(){
        return this.bestCompatibleVersion;
    }

    public void setRequestedVersion(String requestedVersion) throws BadHttpVersionException, HttpParsingException {
        this.bestCompatibleVersion = HttpVersion.getBestCompatibleVersion(requestedVersion);
        if(this.bestCompatibleVersion == null){
            throw new HttpParsingException(HttpStatusCode.HTTP_VERSION_NOT_SUPPORTED);
        }
    }
}
