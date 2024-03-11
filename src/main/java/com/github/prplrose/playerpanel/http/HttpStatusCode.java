package com.github.prplrose.playerpanel.http;

public enum HttpStatusCode {

    /** CLIENT ERRORS */
    BAD_REQUEST(400, "Bad Request"),
    METHOD_NOT_ALLOWED(401, "Method Not Allowed"),
    URI_TOO_LONG(414, "URI Too Long"),
    /** SERVER ERRORS */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED_ERROR(501, "Not Implemented Error"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");

    public final int STATUS_CODE;
    public final String MESSAGE;

    HttpStatusCode(int STATUS_CODE, String MESSAGE) {
        this.STATUS_CODE = STATUS_CODE;
        this.MESSAGE = MESSAGE;
    }
}
