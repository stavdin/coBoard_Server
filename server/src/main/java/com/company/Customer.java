package com.company;

public class Customer {
    private String id;
    private String name;
    private String paymentType;
    //should have a queue of users that are waiting for service

    public Customer(String id, String name, String paymentType){
        this.id = id;
        this.name = name;
        this.paymentType = paymentType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPaymentType() {
        return paymentType;
    }
}
