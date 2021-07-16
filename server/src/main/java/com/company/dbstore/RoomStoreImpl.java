package com.company.dbstore;

import java.sql.*;
import java.util.UUID;

public class RoomStoreImpl implements RoomStore {

    public RoomStoreImpl() {
    }

    @Override
    public String createRoom(String ownerEmail) throws Exception {
        // access db and ask whether such a user exists
        String roomId = UUID.randomUUID().toString();

        String query = "INSERT INTO \"Rooms\" (roomid, owner) VALUES (?,?)";
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/WhiteboardDB", "postgres", "stavssj15394");
             PreparedStatement preparedStatement = con.prepareStatement(query);) {
            preparedStatement.setString(1, roomId);
            preparedStatement.setString(2, ownerEmail);
            preparedStatement.executeUpdate();
            return roomId;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean joinRoom(String roomId) throws Exception {
       String query = "SELECT * FROM \"Rooms\" WHERE \"roomid\" = ?";
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/WhiteboardDB", "postgres", "stavssj15394");
             PreparedStatement preparedStatement = con.prepareStatement(query);) {
            preparedStatement.setString(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();// if it is not empty next is true, so such a user exists and we should let them log in.
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("SQL Error");
        }

    }
}

