package com.hoangdp.forum.controller;

import java.io.IOException;

import com.hoangdp.forum.entity.Post;
import com.hoangdp.forum.service.PostService;
import com.hoangdp.forum.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/post/edit")
public class EditPostController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("editor.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("inputTitle");
        String content = req.getParameter("inputContent");

        if (req.getParameter("id") == null || req.getParameter("id").isBlank()
                || req.getParameter("id").equals("null")) {
            System.out.println("Vo day ne");
            if (PostService.getInstant().create(Post.builder().title(title).content(content)
                    .user(UserService.getInstant().getCurrentUser()).build()) != null) {
                resp.sendRedirect("..");
                return;
            }
        } else {
            if (PostService.getInstant().update(Long.valueOf(req.getParameter("id")),
                    Post.builder().title(title).content(content)
                            .user(UserService.getInstant().getCurrentUser()).build()) != null) {
                resp.sendRedirect("..");
                return;
            }
        }
        super.doPost(req, resp);
    }
}
