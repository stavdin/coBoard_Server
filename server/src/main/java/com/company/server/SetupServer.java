package com.company.server;

import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

public class SetupServer {
    private int port;

    SetupServer(int port) {
        this.port = port;
    }



    Server setupServer() {
        Server server = new Server(new ExecutorThreadPool(10, 1));
        ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory());
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        connector.setPort(port); //db does not listen on this port
        server.addConnector(connector);
        handler.addServlet(UserLoginServlet.class, "/userLogin");
        handler.addServlet(CreateRoomServlet.class, "/createRoom");
        handler.addServlet(JoinRoomServlet.class, "/joinRoom");

//        handler.addServlet(SendMessageServlet.class, "/sendMessage");
//        handler.addServlet(CreateSessionServlet.class, "/createSession");
//        handler.addServlet(WaitForMessageServlet.class, "/waitForMessage");
//        handler.addServlet(EndSessionServlet.class, "/endSession");
        return server;
    }
    public int getPort() {
        return port;
    }
}
