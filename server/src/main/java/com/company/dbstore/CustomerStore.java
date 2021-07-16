package com.company.dbstore;

import com.company.Customer;

public interface CustomerStore {
    Customer getCustomerById(String cid);
    //int getCustomersCount();
    void addNewCustomer(Customer c);
}
