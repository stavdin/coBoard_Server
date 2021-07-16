package com.company.dbstore;

import com.company.Representative;

import java.util.ArrayList;

public class RepresentativeStoreImpl implements RepresentativeStore {
    //singleton class
    //private static final RepresentativeStoreImpl instance = new RepresentativeStoreImpl();

    public RepresentativeStoreImpl(){

    }

//    public static RepresentativeStoreImpl getInstance(){
//        return instance;
//    }

    @Override
    public Representative getRepresentativeByEmail(String email) {
        return null;
    }

    @Override
    public ArrayList<Representative> getAvailableRepresentativesOfCustomer(String customerId) {
        return null;
    }

    @Override
    public Representative getRepresentativeById(String rid) {
        return null;
    }

    @Override
    public ArrayList<Representative> getRepresentativesOfCustomer(String customerId) {
        return null;
    }
}
