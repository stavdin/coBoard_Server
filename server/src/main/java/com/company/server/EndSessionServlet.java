package com.company.server;

import com.company.VariableFormat;
import com.company.AppGlobals;
import com.company.dbstore.SessionStore;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EndSessionServlet extends HttpServlet {
    private static VariableFormat format = VariableFormat.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //this is a request

        String sid = req.getParameter("sid");
        String isRepStr = req.getParameter("isRep");
        String messageNum = req.getParameter("messageNum");

        if (!format.isSessionIdFormatOk(sid) || !format.isRepFormatOk(isRepStr) || !format.isMessageNumOk(messageNum)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "must supply valid sid , isRep , messageNum values");
            return;
        }

        AppGlobals appGlobals = AppGlobals.getInstance();
        SessionStore sessionStore = appGlobals.getSessionStore();
        boolean isRep = Boolean.parseBoolean(isRepStr);
        sessionStore.endSession(sid); //end session
        //SessionStoreImpl.getInstance().endSession(sid); //end session
        if (isRep) {
            appGlobals.userPubSub.put(sid, Integer.parseInt(messageNum)); //let user know - when we implement on change, we will check if the other end has disconnected. if so we disconnect too

        } else {
            appGlobals.repPubSub.put(sid, Integer.parseInt(messageNum));//let representative know
        }
        appGlobals.repPubSub.remove(sid);//disconnect representative from session
        appGlobals.userPubSub.remove(sid);//disconnect user from session

        //how do we respond when session is ended?
        JSONObject json = new JSONObject();
        json.put("sessionId", sid);
        resp.getOutputStream().println(json.toString());

    }
}
