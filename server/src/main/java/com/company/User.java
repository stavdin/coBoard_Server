package com.company;

public class User {
    private String sessionRequestTime;
    private String email;
    private String sessionInitiatingMessage;
    private String ofCustomer;

    public User(String from, String msg, String toCustomer, String time){
        this.ofCustomer = toCustomer;
        this.email = from;
        this.sessionInitiatingMessage = msg;
        this.sessionRequestTime = time;
    }


}
