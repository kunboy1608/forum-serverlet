package com.hoangdp.forum.service;

import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.hoangdp.forum.entity.User;
import com.hoangdp.forum.utils.HibernateUtils;

public class UserService extends Service<User, UUID> {

    private static UserService instant;
    private static final Object lock = new Object();

    private UserService() {
        super(User.class);
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
        return super.save(
                User.builder().username(username).password(password).nickname(nickname).salt("").build()) != null;
    }

}
