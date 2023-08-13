package com.hoangdp.forum.service;

import java.sql.Date;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.hoangdp.forum.entity.User;
import com.hoangdp.forum.security.JwtService;
import com.hoangdp.forum.utils.HibernateUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class UserService extends Service<User, UUID> {

    private static UserService instant;
    private static final Object lock = new Object();

    private UserService() {
        super(User.class);
    }

    @Override
    public User create(HttpServletRequest req, User t) {
        return create(t);
    }

    public User create(User t) {
        try (Session s = HibernateUtils.getSessionFactory().openSession()) {

            t.setCreateOn(new Date(System.currentTimeMillis()));
            t.setCreatedBy(null);

            t.setLastModifiedOn(new Date(System.currentTimeMillis()));
            t.setLastModifiedBy(null);

            s.beginTransaction();
            s.persist(t);
            s.flush();
            s.getTransaction().commit();
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static UserService getInstant() {
        if (instant == null) {
            synchronized (lock) {
                if (instant == null) {
                    instant = new UserService();
                }
            }
        }
        return instant;
    }

    public Boolean login(String username, String password) {
        try (Session s = HibernateUtils.getSessionFactory().openSession()) {
            s.beginTransaction();

            NativeQuery<User> query = s.createNativeQuery(
                    "select * from " + getNameTable() + " where username = :username",
                    User.class);

            query.setParameter("username", username);

            // TODO: Implement decode password here
            User u = query.list().get(0);

            if (u == null) {
                return null;
            }

            return u.getPassword().equals(password);

        } catch (IndexOutOfBoundsException e) {
            System.out.printf("Username %s is not exist", username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean signup(String username, String password, String nickname) {
        // TODO: Implement encode password here
        return this.create(
                User.builder().username(username).password(password).nickname(nickname).salt("").build()) != null;
    }

    public User findByUsername(String username) {
        try (Session s = HibernateUtils.getSessionFactory().openSession()) {
            s.beginTransaction();

            NativeQuery<User> query = s.createNativeQuery(
                    "select * from " + getNameTable() + " where username = :username",
                    User.class);

            query.setParameter("username", username);

            return query.list().get(0);

        } catch (IndexOutOfBoundsException e) {
            System.out.printf("Username %s is not exist", username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getCurrentUser(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        String token = "";

        for (Cookie c : cookies) {
            if (c.getName().equals("Authorization")) {
                token = c.getValue();
            }
        }

        if (token == null || token.isBlank()) {
            return null;
        }

        final JwtService jwtService = new JwtService();
        final String username = jwtService.extractUsername(token);

        return findByUsername(username);

    }

}
