package com.company.dbstore;

import com.company.Customer;

import java.sql.*;

public class CustomerStoreImpl implements CustomerStore {
    //singleton class
    private static final CustomerStoreImpl instance = new CustomerStoreImpl();

    public CustomerStoreImpl() {
    }

    public static CustomerStoreImpl getInstance() {
        return instance;
    }

    public Customer getCustomerById(String id) {
        //access db and find a customer by id
        String query = "Select * from public.\"Customers\" where cid = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatProjectDB2", "postgres", "password");
             PreparedStatement prepStmt = connection.prepareStatement(query);) {
            prepStmt.setString(1, id);
            try (ResultSet resultSet = prepStmt.executeQuery();) {
                return new Customer(resultSet.getString("cid"), resultSet.getString("payment_type"), resultSet.getString("customer_name"));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new RuntimeException("SQL Error.");
        }
    }

    ;


//    public int getCustomersCount(){
//        //count number of customers in db
//        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatProjectDB2","postgres","password")){
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("select count(*) as total from public.\"Customers\"");
//            return Integer.parseInt(resultSet.getString("total"));
//
//        }catch(SQLException e1){
//            System.out.println("SQL error..");
//            e1.printStackTrace();
//        }
//        catch(Exception e2){
//            System.out.println("connection to database failed");
//            e2.printStackTrace();
//        }
//        return -1; //error occured
//    };


    public void addNewCustomer(Customer c) {
        //adds new entry to customer table in db, when given a customer object
        String query = "INSERT INTO public.\"Customers\" (cid, payment_type, customer_name) VALUES (?,?,?)";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatProjectDB2", "postgres", "password");
             PreparedStatement prepStmt = connection.prepareStatement(query);) {
            prepStmt.setString(1, c.getId());
            prepStmt.setString(2, c.getPaymentType());
            prepStmt.setString(3, c.getName());
            try (ResultSet resultSet = prepStmt.executeQuery();) {
                //log query result?
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new RuntimeException("SQL Error.");
        }
    }

    ;

}
