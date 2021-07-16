package com.company.server;
import com.company.Customer;
import com.company.User;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

import java.sql.*;
import java.util.ArrayList;


public class ChatServlet extends HttpServlet {

    //...
//    private ArrayList<Customer> customers = new ArrayList<>(); // list of customers that needs to be initialized when server goes up
//    private ArrayList<User> users = new ArrayList<>(); //total list of users waiting for service or in a session with a representative across all customers


    public ChatServlet(){
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        //this is a request

        String to = req.getParameter("to");
        // this will be what defines the conversation like cid. use this to figure out which users need to see the message that was sent
        String toCustomer = req.getParameter("toa");
        // this will be the time sent to be updated in the database
        String time = req.getParameter("time");
        //time of request
        String msg = req.getParameter("msg");
        // this is the message sent
        String from = req.getParameter("frm");
        //this is who sent the message uid.



        // User user = new User(from,msg,toCustomer,time);
        // here we create a new user and insert it to the relevant customer's queue...
        // we need to

        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println("EmbeddedJetty");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatProjectDB2","postgres","password")){
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            System.out.println("Reading customer records");
            ResultSet resultSet = statement.executeQuery("SELECT * from public.\"Customers\"");
            while (resultSet.next()){
                System.out.println(resultSet.getString("cid"));
            }

            while (resultSet.next()){
                System.out.println(resultSet.getString("cid"));
            }

        }catch(SQLException e1){
            System.out.println("SQL error..");
            e1.printStackTrace();
        }
        catch(Exception e2){
            System.out.println("connection to database failed");
            e2.printStackTrace();
        }

    }

}
