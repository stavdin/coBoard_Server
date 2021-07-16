package com.company.dbstore;

import com.company.BadParametersException;
import com.company.SessionState;

public interface SessionStore {
    SessionState validateSessionExistence (String sid);
    String insertSession( String cid, String email);
    void endSession(String sid);
}
