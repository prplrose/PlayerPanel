package com.github.prplrose.playerpanel.http.httpmessage.headers;

public class Headers {

    static public class ContentLength extends AbstractHeader<Integer>{

        public static final String name = "Content-Length";
        int length;

        ContentLength(int length) {
            this.length = length;
        }

        ContentLength(String length) {
            try{
                this.length = Integer.parseInt(length.strip());
            }catch (NumberFormatException e){
                this.length=0;
            }
        }

        @Override
        public Integer getValue() {
            return length;
        }

        @Override
        public String getName(){
            return "Content-Length";
        }

    }

}
