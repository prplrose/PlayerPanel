package com.github.prplrose.playerpanel.http.httpmessage;

import com.github.prplrose.playerpanel.http.HttpMethod;
import com.github.prplrose.playerpanel.http.HttpParsingException;
import com.github.prplrose.playerpanel.http.HttpStatusCode;
import com.github.prplrose.playerpanel.http.HttpVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    @Test
    void correctRequest(){

        ByteArrayInputStream stream = new ByteArrayInputStream("""
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
                """.getBytes(StandardCharsets.US_ASCII));

        try {
            HttpRequest request = new HttpRequest(stream);
            assertNotNull(request);
            Assertions.assertEquals(HttpMethod.GET, request.getRequestHead().getMethod());
            assertEquals("/", request.getRequestHead().getTarget());
        }catch (Exception e){
            fail(e);
        }
    }

    @Test
    void badMethod()  {
        ByteArrayInputStream stream = new ByteArrayInputStream("""
                BONK / HTTP/1.1\r
                Host: localhost:8080\r
                \r
                """.getBytes(StandardCharsets.US_ASCII));
        try {
            new HttpRequest(stream);
            fail();
        }catch (HttpParsingException e){
            Assertions.assertEquals(HttpStatusCode.NOT_IMPLEMENTED_ERROR, e.getErrorCode());
        }
    }

    @Test
    void unsupportedVersion()  {
        ByteArrayInputStream stream = new ByteArrayInputStream("""
                GET / HTTP/3.1\r
                Host: localhost:8080\r
                \r
                """.getBytes(StandardCharsets.US_ASCII));
        try {
            new HttpRequest(stream);
            fail();
        }catch (HttpParsingException e){
            assertEquals(HttpStatusCode.HTTP_VERSION_NOT_SUPPORTED, e.getErrorCode());
        }
    }

    @Test
    void invalidVersion()  {
        ByteArrayInputStream stream = new ByteArrayInputStream("""
                GET / HTTB/1.1\r
                Host: localhost:8080\r
                \r
                """.getBytes(StandardCharsets.US_ASCII));
        try {
            new HttpRequest(stream);
            fail();
        }catch (HttpParsingException e){
            assertEquals(e.getErrorCode(), HttpStatusCode.BAD_REQUEST);
        }
    }

    @Test
    void downgradeVersion()  {
        ByteArrayInputStream stream = new ByteArrayInputStream("""
                GET / HTTP/1.2\r
                Host: localhost:8080\r
                \r
                """.getBytes(StandardCharsets.US_ASCII));
        try {
            HttpRequest request = new HttpRequest(stream);
            assertNotNull(request);
            Assertions.assertEquals(request.getRequestHead().getVersion(), HttpVersion.HTTP_1_1);
        }catch (HttpParsingException e){
            fail();
        }
    }

    @Test
    void tooLongMethodName()  {
        ByteArrayInputStream stream = new ByteArrayInputStream("""
                GETREKTMYMETHODISASBIGASYOURMAMA / HTTP/1.1\r
                Host: localhost:8080\r
                \r
                """.getBytes(StandardCharsets.US_ASCII));
        try {
            new HttpRequest(stream);
            fail();
        }catch (HttpParsingException e){
            assertEquals(e.getErrorCode(), HttpStatusCode.NOT_IMPLEMENTED_ERROR);
        }
    }

    @Test
    void tooManyItems()  {
        ByteArrayInputStream stream = new ByteArrayInputStream("""
                GET / minecraft:diamond HTTP/1.1\r
                Host: localhost:8080\r
                \r
                """.getBytes(StandardCharsets.US_ASCII));
        try {
            new HttpRequest(stream);
            fail();
        }catch (HttpParsingException e){
            assertEquals(e.getErrorCode(), HttpStatusCode.BAD_REQUEST);
        }
    }

    @Test
    void emptyRequest()  {
        ByteArrayInputStream stream = new ByteArrayInputStream("""
                \r
                Host: localhost:8080\r
                \r
                """.getBytes(StandardCharsets.US_ASCII));
        try {
            new HttpRequest(stream);
            fail();
        }catch (HttpParsingException e){
            assertEquals(e.getErrorCode(), HttpStatusCode.BAD_REQUEST);
        }
    }

    @Test
    void noLineFeed()  {
        ByteArrayInputStream stream = new ByteArrayInputStream("""
                GET / HTTP/1.1\rHost: localhost:8080\r
                \r
                """.getBytes(StandardCharsets.US_ASCII));
        try {
            new HttpRequest(stream);
            fail();
        }catch (HttpParsingException e){
            assertEquals(e.getErrorCode(), HttpStatusCode.BAD_REQUEST);
        }
    }

    @Test
    void hasBody()  {
        ByteArrayInputStream stream = new ByteArrayInputStream("""
                GET / HTTP/1.1\r
                Host: localhost:8080\r
                Content-Length: 12\r
                \r
                Hello world!
                """.getBytes(StandardCharsets.US_ASCII));
        try {
            HttpRequest request = new HttpRequest(stream);
            assertEquals("Hello world!", request.body);
        }catch (HttpParsingException e){
            fail();
        }
    }

    @Test
    void negativeBodyLength()  {
        ByteArrayInputStream stream = new ByteArrayInputStream("""
                GET / HTTP/1.1\r
                Host: localhost:8080\r
                Content-Length: -5\r
                \r
                Hello world!
                """.getBytes(StandardCharsets.US_ASCII));
        try {
            HttpRequest request = new HttpRequest(stream);
            assertEquals("", request.body);
        }catch (HttpParsingException e){
            fail();
        }
    }

    @Test
    void notMatchingBodyLength()  {
        ByteArrayInputStream stream = new ByteArrayInputStream("""
                GET / HTTP/1.1\r
                Host: localhost:8080\r
                Content-Length: 15\r
                \r
                Hello world!
                """.getBytes(StandardCharsets.US_ASCII));
        try {
            new HttpRequest(stream);
        }catch (HttpParsingException e){
            fail();
        }
    }

}