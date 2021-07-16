package com.company.dbstore;

import com.company.pubsub.PubSubStruct;

import java.sql.*;

public class PubSubStoreImpl implements PubSubStore {
    //singleton class
    // private static final PubSubStoreImpl instance = new PubSubStoreImpl();


    public PubSubStoreImpl() {
    }

//    public static PubSubStoreImpl getInstance() {
//        return instance;
//    }

    @Override
    public void fetchPubSubFromDatabase(PubSubStruct<String, Integer> pubSub) {
        //
        //validate that the session exists
        String query = "SELECT sid from public.\"Sessions\" where is_over = false"; //select all current sessions from db
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatProjectDB2", "postgres", "stavssj15394");
             Statement s = connection.createStatement();
             ResultSet resultSet = s.executeQuery(query);) {
            while (resultSet.next()) {
                pubSub.put(resultSet.getString("sid"), null); //we initialize pubsub by getting all non over sessions from db.
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new RuntimeException("sql error");
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new RuntimeException("database connection error");
        }
    }
}



