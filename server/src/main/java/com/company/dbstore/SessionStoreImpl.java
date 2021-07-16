package com.company.dbstore;

import com.company.AppGlobals;
import com.company.SessionState;

import java.sql.*;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.UUID;


public class SessionStoreImpl implements SessionStore {
   // private static final SessionStoreImpl instance = new SessionStoreImpl();

    public SessionStoreImpl() {
    }

//    public static SessionStoreImpl getInstance() {
//
//        return instance;
//    }

    @Override
    public SessionState validateSessionExistence(String sid) {
        String query = "SELECT is_over FROM public.\"Sessions\" WHERE sid = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatProjectDB2", "postgres", "password");
             PreparedStatement prepStmt = connection.prepareStatement(query);) {
            prepStmt.setString(1, sid);
            try (ResultSet validate = prepStmt.executeQuery();) {
                if (validate.next()) {
                    if (validate.getBoolean("is_over")) {
                        return SessionState.EXISTS_AND_ENDED; //session exists - but has ended
                    }
                    return SessionState.EXISTS_AND_NOT_ENDED; //session exists and is not over
                }
                return SessionState.NOT_EXISTS;//session does not exist
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new RuntimeException("SQL error..");
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new RuntimeException("connection to database failed");
        }

    }

    @Override
    public void endSession(String sessionId) {

        //validate that the session exists
        if (!(validateSessionExistence(sessionId) == SessionState.EXISTS_AND_NOT_ENDED)) {
            throw new IllegalArgumentException("nonexistent session, or already ended");
        }

        String updateQuery = "Update public.\"Sessions\" SET is_over=true where sid = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatProjectDB2", "postgres", "password");
             PreparedStatement prep = connection.prepareStatement(updateQuery);) {
            //execute query to change session to over, let listeners know and return.
            prep.setString(1, sessionId);
            try {
                prep.executeUpdate();
            } catch (SQLException e1) {
                e1.printStackTrace();
                throw new RuntimeException("SQL Error.");
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new RuntimeException("DB connection error.");
        }
    }


    @Override
    public String insertSession(String cid, String email) {
        // this method inserts a new row to session table. returns inserted sessionId
        // sid is session id and cid is customer id
        String newSessionId = UUID.randomUUID().toString(); //ne need to check it is really unique
        if (validateSessionExistence(newSessionId) != SessionState.NOT_EXISTS) {
            throw new RuntimeException("randomized UUID already appears in db");
        }

        String insertQuery = "INSERT INTO public.\"Sessions\" (sid,rid,creation_time,end_time,cid,user_email,rep_feedback,rating,is_over) VALUES (?,?,?,?,?,?,?,?,?) ";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatProjectDB2", "postgres", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);) {
            preparedStatement.setString(1, newSessionId);
            preparedStatement.setNull(2, Types.VARCHAR); //there is no representative assigned to the session yet, so we insert NULL
            preparedStatement.setString(3,  AppGlobals.df.format(Calendar.getInstance().getTime())); // a session starts when it is requested
            preparedStatement.setNull(4, Types.VARCHAR);
            preparedStatement.setString(5, cid);
            preparedStatement.setString(6, email);
            preparedStatement.setNull(7, Types.VARCHAR);
            preparedStatement.setNull(8, Types.INTEGER);
            preparedStatement.setBoolean(9, false);
            try {
                preparedStatement.executeUpdate();
                return newSessionId;
            } catch (SQLException e1) {
                e1.printStackTrace();
                throw new RuntimeException("sql error");

            }
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new RuntimeException("connection to db failed");
        }
    }
}
