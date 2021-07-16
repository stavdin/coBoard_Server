package com.company;

public class BadParametersException extends Exception {
    public String str;
    public BadParametersException(String str){
        this.str = str;
    }

}
