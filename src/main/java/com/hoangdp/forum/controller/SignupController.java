package com.hoangdp.forum.controller;

import java.io.IOException;

import com.hoangdp.forum.security.JwtService;
import com.hoangdp.forum.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/auth/signup")
public class SignupController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/auth/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("inputUsername").trim().toLowerCase();
        String password = req.getParameter("inputPassword");
        String nickname = req.getParameter("inputNickname");
        String avatar = req.getParameter("inputAvatar");

        System.out.println(req.getRequestURL());

        if (UserService.getInstant().signup(username, password, nickname, avatar)) {
            JwtService jwtService = new JwtService();

            Cookie cookie = new Cookie("Authorization",
                    jwtService.generateToken(UserService.getInstant().findByUsername(username)));
            cookie.setPath("/");
            cookie.setMaxAge(600);
            resp.addCookie(cookie);
            resp.sendRedirect("..");
            return;
        }
        super.doPost(req, resp);
    }
}
