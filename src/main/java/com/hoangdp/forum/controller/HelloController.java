package com.hoangdp.forum.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import com.hoangdp.forum.entity.Post;
import com.hoangdp.forum.entity.User;
import com.hoangdp.forum.service.PostService;
import com.hoangdp.forum.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/hello")
public class HelloController extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("Init khong tham so alloooo");
        super.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Bat duoc k ne 1");
        System.out.println(req.getMethod());
        super.service(req, resp);
    }

    @Override
    public void destroy() {
        System.out.println("destroy ne");
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {                                

        UserService userService = new UserService();
        User u = userService.save(User.builder().username("admin").password("admin").nickname("ADMIN").avatar("1").build());
        System.out.println(u.getId());
        
        PostService postService = new PostService();
        postService.save(new Post());

        resp.setContentType(ContentType.Text.HTML);
        Enumeration<String> header = req.getHeaderNames();

        while (header.hasMoreElements()) {
            String key = header.nextElement();
            System.out.println(key + ":" + req.getHeader(key));
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.println("<h1>Hello 2</h1>");
        printWriter.println("<p>Hello 2</p>");
        printWriter.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}
