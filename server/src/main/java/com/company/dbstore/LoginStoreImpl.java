package com.company.dbstore;

import java.sql.*;

public class LoginStoreImpl implements LoginStore {

    public LoginStoreImpl() {
    }

    @Override
    public boolean userLogin(String email, String password) {
        // access db and ask whether such a user exists

        String query = "Select * from \"Users\" where \"Email\" = ? AND \"Password\" = ?";
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/WhiteboardDB", "postgres", "stavssj15394");
             PreparedStatement preparedStatement = con.prepareStatement(query);) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();// if it is not empty next is true, so such a user exists and we should let them log in.
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("SQL Error");
        }
    }
}
