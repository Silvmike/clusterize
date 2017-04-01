package ru.hardcoders.simple;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Michael on 04.04.2015.
 */
public class TestServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (session.isNew()) {
            session.setAttribute("mySessKey", new Date().toString());
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
