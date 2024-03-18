package com.github.prplrose.playerpanel.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpVersionTest {

    @Test
    void exactMatch(){
        HttpVersion version = null;
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
        }catch (HttpException e){
            fail();
        }
        assertNotNull(version);
        assertEquals(version, HttpVersion.HTTP_1_1);
    }

    @Test
    void badFormat(){
        try {
            HttpVersion.getBestCompatibleVersion("http/1.1");
            fail();
        }catch (HttpException e){
            assertEquals(HttpStatusCode.BAD_REQUEST, e.getErrorCode());
        }
    }

    @Test
    void higherVersion(){
        HttpVersion version = null;
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.2");
        }catch (HttpException e){
            fail();
        }
        assertNotNull(version);
        assertEquals(version, HttpVersion.HTTP_1_1);
    }

}