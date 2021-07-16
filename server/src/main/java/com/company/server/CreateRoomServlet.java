package com.company.server;


import com.company.AppGlobals;
import com.company.VariableFormat;
import com.company.dbstore.RoomStore;
import com.company.dbstore.SessionStore;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateRoomServlet extends HttpServlet {
    private static VariableFormat format = VariableFormat.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //this is a request
        //http://localhost:8081/createRoom?email=amit@trugman.com
        String ownerEmail = req.getParameter("email");
        if (!format.isEmailFormatOk(ownerEmail)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid customer id or email format");
            return;
        }

        AppGlobals appGlobals = AppGlobals.getInstance();
        RoomStore roomStore = appGlobals.getRoomStore();
        try {
            String resultRoomId = roomStore.createRoom(ownerEmail);
            JSONObject json = new JSONObject();
            json.put("roomId", resultRoomId);
            resp.getOutputStream().println(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error");
        }

    }
}
