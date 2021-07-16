package com.company;

public class Representative {
    private String email;
    private String rid;
    private String password;
    private String customerId;

    public Representative(String email, String rid, String password, String customerId){
        this.customerId=customerId;
        this.email = email;
        this.password=password;
        this.rid = rid;
    }

    public String getEmail(){return this.email;}
    public String getRid(){return this.rid;}
    public String getPassword(){return this.password;}
    public String getCustomerId(){return this.customerId;}
}
