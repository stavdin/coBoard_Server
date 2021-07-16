package com.company.pubsub;

public class PubSubException extends Exception {
    private String str;
    PubSubException(String str){this.str = str;}
}
