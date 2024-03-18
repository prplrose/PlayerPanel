package com.github.prplrose.playerpanel.webpage;

import com.github.prplrose.playerpanel.PlayerPanel;
import com.github.prplrose.playerpanel.http.HttpException;
import com.github.prplrose.playerpanel.http.HttpStatusCode;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

public class PageManager {

    final static HashMap<URI, Page> pages = new LinkedHashMap<>();
    final static Logger LOGGER = PlayerPanel.LOGGER;

    public static void load(File directory) throws IOException {
        LOGGER.info("loading webpage: " + directory);
        try {
            for(File file : Objects.requireNonNull(directory.listFiles())){
                if (file.isDirectory())
                    continue;
                String filename = file.toString();
                if(filename.endsWith(".html")){
                    filename = filename.substring(0,filename.length()-5);
                }
                URI fileURI = directory.toURI().relativize(new File(filename).toURI());
                pages.put(fileURI, new Page(file));
                LOGGER.info("registered: " + directory.toURI().relativize(file.toURI()).getPath());
            }
        }catch (NullPointerException ignored){}
    }

    public static String getPage(URI uri) throws HttpException {
        Page page = pages.get(uri);
        if(page==null){
            throw new HttpException(HttpStatusCode.NOT_FOUND);
        }
        return page.getPage();
    }
}
