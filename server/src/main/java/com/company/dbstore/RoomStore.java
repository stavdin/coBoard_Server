package com.company.dbstore;

import java.util.UUID;

public interface RoomStore {
    String createRoom(String ownerEmail) throws Exception;

    //list<changes> joinExistingRoom();
    boolean joinRoom(String roomId) throws Exception;
}
