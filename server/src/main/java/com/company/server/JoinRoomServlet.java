package com.company.server;

import com.company.AppGlobals;
import com.company.VariableFormat;
import com.company.dbstore.LoginStore;
import com.company.dbstore.RoomStore;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class JoinRoomServlet extends HttpServlet {
    private static VariableFormat format = VariableFormat.getInstance();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        req.getParameter("roomid");
        while (reader.ready()) {
            buffer.append(reader.readLine());
        }
        try {
            JSONObject jsonObject = new JSONObject(buffer.toString());
            String roomId = jsonObject.getString("roomid");
//          String userEmail = jsonObject.getString("email"


            // check validity of retrieved data
//        if (!(format.isEmailFormatOk(userEmail))) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid email format");
//        }
            if (!(format.isPasswordFormatOk(roomId))) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid roomId format");
            }

            //ask db if such a user exists - if not send back a json  with error string. if yes, send back a json with authentication number??
            AppGlobals appGlobals = AppGlobals.getInstance();
            RoomStore roomStore = appGlobals.getRoomStore();
            boolean b = roomStore.joinRoom(roomId);

            //respond with a jsonObject that has the boolean value - whether such a roomId exists.
            JSONObject json = new JSONObject();
            json.put("roomNumberFound", b);
            //resp.getOutputStream().println(json.toString());
            resp.getWriter().println(json.toString());
        } catch (JSONException e) {
            // report an error
            System.out.print("failed to retrieve POST data");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}