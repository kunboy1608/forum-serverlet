package com.hoangdp.forum.controller;

import java.io.IOException;

import com.hoangdp.forum.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/auth/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/auth/login.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType(ContentType.Text.HTML);

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Boolean result = UserService.getInstant().login(username, password);

        if (result == null) {
            req.setAttribute("msg",
                    """
                            <div class="mb-3 text-danger">
                                <p>Username is not exist</p>
                            </div>
                                    """);
        } else if (Boolean.TRUE.equals(result)) {
            resp.sendRedirect("..");
            return;
        } else {
            req.setAttribute("msg",
                    """
                            <div class="mb-3 text-danger">
                                <p>Wrong pasword</p>
                            </div>
                                    """);
        }

        req.getRequestDispatcher("/auth/login.jsp").forward(req, resp);
    }

}
