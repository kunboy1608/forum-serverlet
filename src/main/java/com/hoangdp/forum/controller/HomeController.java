package com.hoangdp.forum.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.hoangdp.forum.entity.Post;
import com.hoangdp.forum.service.PostService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/")
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        
        // Allow resource js and css
        if (path.startsWith("/js/") || path.startsWith("/css/")) {
            // Serve static resource
            String resourcePath = getServletContext().getRealPath(path);
            File file = new File(resourcePath);
            if (file.exists()) {
                resp.setContentType(getServletContext().getMimeType(resourcePath));
                Files.copy(file.toPath(), resp.getOutputStream());
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            List<Post> list = PostService.getInstant().findAll();            
            req.setAttribute("list", list);
            req.getRequestDispatcher("post/index.jsp").forward(req, resp);
        }
    }

}
