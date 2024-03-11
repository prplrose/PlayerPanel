package com.github.prplrose.playerpanel.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpVersionTest {

    @Test
    void exactMatch(){
        HttpVersion version = null;
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
        }catch (BadHttpVersionException e){
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
        }catch (BadHttpVersionException ignored){
        }
    }

    @Test
    void higherVersion(){
        HttpVersion version = null;
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.2");
        }catch (BadHttpVersionException e){
            fail();
        }
        assertNotNull(version);
        assertEquals(version, HttpVersion.HTTP_1_1);
    }

}