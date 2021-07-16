package com.company.server;

import com.company.VariableFormat;
import com.company.AppGlobals;
import com.company.dbstore.MessageStore;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class SendMessageServlet extends HttpServlet {
    private static VariableFormat format = VariableFormat.getInstance();


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        while(reader.ready()){
            buffer.append(reader.readLine());
        }
        String msg = buffer.toString(); //string now has message
        String sessionId = req.getParameter("sid");
        String isRepStr = req.getParameter("isRep");
        String msgNumStr = req.getParameter("messageNum");
        if (!format.isRepFormatOk(isRepStr) || !format.isSessionIdFormatOk(sessionId) || !format.isMessageFormatOk(msg) || !format.isMessageNumOk(msgNumStr)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid sessionId, message, or isRep format");
            return;
        }

        int msgNum = Integer.parseInt(msgNumStr);
        boolean isRep = Boolean.parseBoolean(isRepStr);
        AppGlobals appGlobals = AppGlobals.getInstance();
        MessageStore messageStore = appGlobals.getMessageStore();
        messageStore.insertMessage(sessionId, msg, isRep, msgNum); //put message line in db
        // now we need to wake up the representative/user waiting for that message - we do it by changing the value of the node in pubSub in the next line

        if (isRep) {
            //if we get here it means the message was initiated by a representative
            appGlobals.repPubSub.put(sessionId, msgNum);
        } else {
            //if we get here it means the message was initiated by a user
            appGlobals.userPubSub.put(sessionId, msgNum);
        }
        JSONObject json = new JSONObject();
        json.put("messageSent", true);
        resp.getOutputStream().println(json.toString());
    }
}


