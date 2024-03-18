package com.github.prplrose.playerpanel.webpage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Page {

    final String page;

    Page(File htmlFile) throws IOException {
        FileReader reader = new FileReader(htmlFile);
        StringBuilder buffer = new StringBuilder();
        int c;
        while ( (c = reader.read()) != -1 ){
            buffer.append((char) c);
        }
        reader.close();
        this.page = buffer.toString();
    }

    String getPage(){
        return this.page;
    }

}
