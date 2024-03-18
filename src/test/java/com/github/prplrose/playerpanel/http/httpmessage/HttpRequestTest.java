package com.github.prplrose.playerpanel.http.httpmessage;

import com.github.prplrose.playerpanel.http.HttpException;
import com.github.prplrose.playerpanel.http.HttpStatusCode;
import com.github.prplrose.playerpanel.http.HttpVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

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
        }catch (HttpException e){
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
        }catch (HttpException e){
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
        }catch (HttpException e){
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
        }catch (HttpException e){
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
        }catch (HttpException e){
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
        }catch (HttpException e){
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
        }catch (HttpException e){
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
        }catch (HttpException e){
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
        }catch (HttpException e){
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
        }catch (HttpException e){
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
        }catch (HttpException e){
            fail();
        }
    }

}