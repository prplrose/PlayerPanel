package com.github.prplrose.playerpanel.http;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HttpParserTest {

    @Test
    void parseRequest(){
        try {
            HttpRequest request = HttpParser.parse(correctRequest());
            assertNotNull(request);
            assertEquals(HttpMethod.GET, request.getMethod());
            assertEquals("/", request.getTarget());
        }catch (Exception e){
            fail(e);
        }
    }

    @Test
    void parseBadMethod()  {
        try {
            HttpParser.parse(badMethodName());
            fail();
        }catch (HttpParsingException e){
            assertEquals(HttpStatusCode.NOT_IMPLEMENTED_ERROR, e.getErrorCode());
        }
    }

    @Test
    void parseUnsupportedVersion()  {
        try {
            HttpParser.parse(unsupportedVersion());
            fail();
        }catch (HttpParsingException e){
            assertEquals(HttpStatusCode.HTTP_VERSION_NOT_SUPPORTED, e.getErrorCode());
        }
    }

    @Test
    void parseBadVersion()  {
        try {
            HttpParser.parse(badVersion());
            fail();
        }catch (HttpParsingException e){
            assertEquals(e.getErrorCode(), HttpStatusCode.BAD_REQUEST);
        }
    }

    @Test
    void parseSupportedVersion()  {
        try {
            HttpRequest request = HttpParser.parse(supportedVersion());
            assertNotNull(request);
            assertEquals(request.getCompatibleVersion(), HttpVersion.HTTP_1_1);
        }catch (HttpParsingException e){
            fail();
        }
    }

    @Test
    void parseTooLong()  {
        try {
            HttpParser.parse(tooLongMethodName());
            fail();
        }catch (HttpParsingException e){
            assertEquals(e.getErrorCode(), HttpStatusCode.NOT_IMPLEMENTED_ERROR);
        }
    }

    @Test
    void parseTooManyItems()  {
        try {
            HttpParser.parse(tooManyItems());
            fail();
        }catch (HttpParsingException e){
            assertEquals(e.getErrorCode(), HttpStatusCode.BAD_REQUEST);
        }
    }

    @Test
    void parseEmptyRequest()  {
        try {
            HttpParser.parse(emptyRequest());
            fail();
        }catch (HttpParsingException e){
            assertEquals(e.getErrorCode(), HttpStatusCode.BAD_REQUEST);
        }
    }

    @Test
    void parseNoLF()  {
        try {
            HttpParser.parse(noLF());
            fail();
        }catch (HttpParsingException e){
            assertEquals(e.getErrorCode(), HttpStatusCode.BAD_REQUEST);
        }
    }

    private InputStream correctRequest(){
        String data = """
                GET / HTTP/1.1\r
                Host: localhost:8080\r
                User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0\r
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r
                Accept-Language: pl,en-US;q=0.7,en;q=0.3\r
                Accept-Encoding: gzip, deflate, br\r
                Connection: keep-alive\r
                Upgrade-Insecure-Requests: 1\r
                Sec-Fetch-Dest: document\r
                Sec-Fetch-Mode: navigate\r
                Sec-Fetch-Site: none\r
                Sec-Fetch-User: ?1\r
                \r
                """;
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream badMethodName(){
        String data = """
                get / HTTP/1.1\r
                Host: localhost:8080\r
                \r
                """;
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream tooLongMethodName(){
        String data = """
                GETREKTMYMETHODISASBIGASYOURMAMA / HTTP/1.1\r
                Host: localhost:8080\r
                \r
                """;
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream tooManyItems(){
        String data = """
                GET / minecraft:diamond HTTP/1.1\r
                Host: localhost:8080\r
                \r
                """;
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream emptyRequest(){
        String data = """
                \r
                Host: localhost:8080\r
                \r
                """;
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream noLF(){
        String data = """
                GET / HTTP/1.1\rHost: localhost:8080\r
                \r
                """;
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream badVersion(){
        String data = """
                GET / HTTB/1.1\r
                Host: localhost:8080\r
                \r
                """;
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream unsupportedVersion(){
        String data = """
                GET / HTTP/3.14\r
                Host: localhost:8080\r
                \r
                """;
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream supportedVersion(){
        String data = """
                GET / HTTP/1.2\r
                Host: localhost:8080\r
                \r
                """;
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.US_ASCII));
    }

}