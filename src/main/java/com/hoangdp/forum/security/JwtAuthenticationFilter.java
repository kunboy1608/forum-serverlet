package com.hoangdp.forum.security;

import java.io.IOException;

import com.hoangdp.forum.entity.User;
import com.hoangdp.forum.service.UserService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter({ "/post/edit" })
public class JwtAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper((HttpServletRequest) request);

        Cookie[] cookies = requestWrapper.getCookies();

        String token = "";

        for (Cookie c : cookies) {
            if (c.getName().equals("Authorization")) {
                token = c.getValue();
            }
        }

        if (token == null || token.isBlank()) {
            // filterChain.doFilter(request, response);
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final JwtService jwtService = new JwtService();
        final String username = jwtService.extractUsername(token);

        if (username != null && !username.isEmpty()) {
            User user = UserService.getInstant().findByUsername(username);
            System.out.println("User name get" + user.getUsername());
            if (jwtService.isTokenValid(token, user)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
