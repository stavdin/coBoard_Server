package com.company.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface JoinRoom {
    void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
