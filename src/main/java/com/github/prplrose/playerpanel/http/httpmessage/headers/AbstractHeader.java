package com.github.prplrose.playerpanel.http.httpmessage.headers;

public abstract class AbstractHeader<T> {

    public static final String name = "";

    public String getName(){
        return name;
    }

    public abstract T getValue();

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    public boolean equals(AbstractHeader<?> header){
        return this.getName().equalsIgnoreCase(header.getName());
    }

    public boolean strictlyEquals(AbstractHeader<?> header){
        boolean equalValues = this.getValue().equals(header.getValue());
        return equalValues && equals(header);
    }

    public boolean equals(String headerName){
        return this.getName().equalsIgnoreCase(headerName);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
