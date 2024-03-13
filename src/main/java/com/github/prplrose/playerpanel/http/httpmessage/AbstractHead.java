package com.github.prplrose.playerpanel.http.httpmessage;

import com.github.prplrose.playerpanel.http.httpmessage.headers.HeaderManager;

public abstract class AbstractHead {

    abstract public HeaderManager getHeaderManager();

    abstract public String toString();

}
