package com.company.server;

import com.company.VariableFormat;
import com.company.dbstore.SessionStore;
import com.company.AppGlobals;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CreateSessionServlet extends HttpServlet {
    private static VariableFormat format = VariableFormat.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //this is a request
        String cid = req.getParameter("cid");
        String email = req.getParameter("email");

        if (!format.isCustomerIdFormatOk(cid) || !format.isEmailFormatOk(email)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid customer id or email format");
            return;
        }

        AppGlobals appGlobals = AppGlobals.getInstance();
        SessionStore sessionStore =  appGlobals.getSessionStore();

        String sessionId = sessionStore.insertSession(cid,email);
        //String sessionId = SessionStoreImpl.getInstance().insertSession(cid, email); // a unique session id is created inside
        appGlobals.userPubSub.put(sessionId, null); //upon creation of a session we insert it to both pubSubs
        appGlobals.repPubSub.put(sessionId, null);
        //now we respond with session id
        JSONObject json = new JSONObject();
        json.put("sessionId", sessionId);
        resp.getOutputStream().println(json.toString());
    }
}