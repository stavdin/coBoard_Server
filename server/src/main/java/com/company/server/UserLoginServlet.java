package com.company.server;

import com.company.AppGlobals;
import com.company.BadParametersException;
import com.company.VariableFormat;
import com.company.dbstore.LoginStore;
import com.company.dbstore.MessageStore;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class UserLoginServlet extends HttpServlet {
    private static final VariableFormat format = VariableFormat.getInstance();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        req.getParameter("pass");
        while (reader.ready()) {
            buffer.append(reader.readLine());
        }
        try {
            JSONObject jsonObject = new JSONObject(buffer.toString());
            String password = jsonObject.getString("password");
            String email = jsonObject.getString("email");


            // check validity of retrieved data
            if (!(format.isEmailFormatOk(email))) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid email format");
            }
            if (!(format.isPasswordFormatOk(password))) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid password format");
            }

            //ask db if such a user exists - if not send back a json  with error string. if yes, send back a json with authentication number??
            AppGlobals appGlobals = AppGlobals.getInstance();
            LoginStore loginStore = appGlobals.getLoginStore();
            boolean b = loginStore.userLogin(email, password);

            //respond with a jsonObject that has the boolean value - whether such a user exists.
            JSONObject json = new JSONObject();
            json.put("userFound", b);
            //resp.getOutputStream().println(json.toString());
            resp.getWriter().println(json.toString());
        } catch (JSONException e) {
            // report an error
            System.out.print("failed to retrieve POST data");
            e.printStackTrace();
        }
    }
}
