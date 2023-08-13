package com.hoangdp.forum.controller;

import java.io.IOException;
import java.util.Random;

import com.hoangdp.forum.entity.Comment;
import com.hoangdp.forum.entity.Post;
import com.hoangdp.forum.service.PostService;
import com.hoangdp.forum.service.UserService;
import com.hoangdp.forum.service.CommentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/post")
public class PostController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));

        if (id == null) {
            super.doPost(req, resp);
            return;
        }

        Post p = PostService.getInstant().findById(Long.valueOf(req.getParameter("id")));

        if (p == null) {
            super.doGet(req, resp);
        }

        req.setAttribute("list", CommentService.getInstant().findAllByPostId(id));

        p.setNumberOfComments(CommentService.getInstant().countByPostId(id));
        p.setNumberOfHearts(new Random().nextLong(10000));

        req.setAttribute("post", p);
        req.setAttribute("currentUser", UserService.getInstant().getCurrentUser(req));

        req.getRequestDispatcher("post/details.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long postId = Long.valueOf(req.getParameter("id"));

        String content = req.getParameter("inputContent");

        Post p = PostService.getInstant().findById(postId);

        if (CommentService.getInstant().create(req, Comment.builder().post(p).content(content)
                .user(UserService.getInstant().getCurrentUser(req)).build()) != null) {
            doGet(req, resp);
            return;
        }
        super.doPost(req, resp);
    }

}
