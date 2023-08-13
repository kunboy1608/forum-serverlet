package com.hoangdp.forum.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.hoangdp.forum.entity.Comment;
import com.hoangdp.forum.utils.HibernateUtils;

public class CommentService extends Service<Comment, Long> {
    private static CommentService instant;
    private static final Object lock = new Object();

    private CommentService() {
        super(Comment.class);
    }

    public static CommentService getInstant() {
        if (instant == null) {
            synchronized (lock) {
                if (instant == null) {
                    instant = new CommentService();
                }
            }
        }
        return instant;
    }

    public Long countByPostId(Long id) {
        try (Session s = HibernateUtils.getSessionFactory().openSession()) {
            s.beginTransaction();
            NativeQuery<Long> query = s.createNativeQuery("""
                    select count(*)
                    from comments c
                    where c.post_id = :post_id
                    """, Long.class);

            query.setParameter("post_id", id);
            return query.list().get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public List<Comment> findAllByPostId(Long id) {
        try (Session s = HibernateUtils.getSessionFactory().openSession()) {
            s.beginTransaction();
            NativeQuery<Comment> query = s.createNativeQuery("""
                    select *
                    from comments c
                    where c.post_id = :post_id
                    """, Comment.class);

            query.setParameter("post_id", id);
            return query.list();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}