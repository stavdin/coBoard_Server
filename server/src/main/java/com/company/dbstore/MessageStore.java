package com.company.dbstore;

import com.company.BadParametersException;
import com.company.Message;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

public interface MessageStore {
    public void insertMessage(String sessionId, String msg, boolean isRep, int messageNum);
    public ArrayList<Message> retrieveAllMessagesSince(String sid, Integer syncpoint);
}
