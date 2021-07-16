package com.company.dbstore;

import com.company.AppGlobals;
import com.company.Message;
import com.company.SessionState;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

public class MessageStoreImpl implements MessageStore {
    //private static final MessageStoreImpl instance = new MessageStoreImpl();

    public MessageStoreImpl() {
    }


//    public static MessageStoreImpl getInstance() {
//        return instance;
//    }


    public ArrayList<Message> retrieveAllMessagesSince( String sid, Integer syncpoint) {
        SessionStore inst = AppGlobals.getInstance().getSessionStore();
        //firstly check if such session exists. if it is and the session isnt over, retrieve all subsequent messages...
        if (inst.validateSessionExistence(sid) != SessionState.EXISTS_AND_NOT_ENDED) {
            throw new IllegalArgumentException("either session id does not exist, or session is already over");
        }

        String query = "Select message_num, test from public.\"MESSAGES\" where sid = ? AND message_num > ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatProjectDB2", "postgres", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sid);
            preparedStatement.setInt(2, syncpoint);
            try (ResultSet resultSet = preparedStatement.executeQuery(query);) {
                ArrayList<Message> arr = new ArrayList<Message>();
                while (resultSet.next()) {
                    int currentMessageNum = resultSet.getInt("message_num");
                    arr.add(new Message(resultSet.getString("text"), false, currentMessageNum));
                }
                return arr;
            }
        } catch (SQLException e) {
            throw new RuntimeException("sql error");
        }
    }

    public void insertMessage(String sessionId, String msg, boolean isRep, int messageNum) {
        SessionStore inst = AppGlobals.getInstance().getSessionStore();
        //firstly check if such session exists. if it is and the session isnt over, put a relevant message entry in db.
        if (inst.validateSessionExistence(sessionId) != SessionState.EXISTS_AND_NOT_ENDED) {
            throw new IllegalArgumentException("either session id does not exist, or session is already over");
        }

        if (messageNum > 2) {
            String query2 = "Select msg_id FROM public.\"MESSAGES\" WHERE sid = ? AND message_number = ? AND is_answer = ?";
            try (Connection connection1 = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatProjectDB2", "postgres", "password");
                 PreparedStatement prepStatement2 = connection1.prepareStatement(query2)) {//check that the previous message is in db
                prepStatement2.setString(1, sessionId);
                prepStatement2.setInt(2, messageNum - 1);
                prepStatement2.setBoolean(3, isRep);
                try (ResultSet validate2 = prepStatement2.executeQuery(query2);) {
                    if (!validate2.next()) {
                        throw new IllegalArgumentException("given message number cannot exist because a previous message doesnt exist");
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                throw new RuntimeException("SQL exception");
            }
        }

        String messageId = UUID.randomUUID().toString(); // we assume uuid is unique..
        String insertQuery = "INSERT INTO public.\"MESSAGES\" (msg_id,sid,text,date_sent,is_answer,message_number) VALUES (?,?,?,?,?,?) ";
        try (Connection connection2 = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatProjectDB2", "postgres", "password");
             PreparedStatement preparedStatement = connection2.prepareStatement(insertQuery);) {
            preparedStatement.setString(1, messageId);
            preparedStatement.setString(2, sessionId); //there is no representative assigned to the session yet, so we insert NULL
            preparedStatement.setString(3, msg); // a session starts when it is requested
            preparedStatement.setString(4, LocalTime.now().toString());
            preparedStatement.setBoolean(5, isRep);
            preparedStatement.setInt(6, messageNum);
            try (ResultSet set = preparedStatement.executeQuery(insertQuery);) {
                //log? query result?
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
            throw new RuntimeException("SQL exception");
        }
    }
}