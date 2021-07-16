package com.company.dbstore;

import com.company.Representative;

import java.util.ArrayList;

public interface RepresentativeStore {
    public Representative getRepresentativeById(String rid);
    public Representative getRepresentativeByEmail(String email);
    public ArrayList<Representative> getRepresentativesOfCustomer(String customerId);
    public ArrayList<Representative> getAvailableRepresentativesOfCustomer(String customerId);
    //...

}
