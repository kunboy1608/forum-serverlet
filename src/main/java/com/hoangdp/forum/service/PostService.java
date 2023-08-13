package com.hoangdp.forum.service;

import java.util.List;
import java.util.Random;

import com.hoangdp.forum.entity.Post;

public class PostService extends Service<Post, Long> {

    private static PostService instant;
    private static final Object lock = new Object();

    private PostService() {
        super(Post.class);
    }

    public static PostService getInstant() {
        if (instant == null) {
            synchronized (lock) {
                if (instant == null) {
                    instant = new PostService();
                }
            }
        }
        return instant;
    }

    @Override
    public List<Post> findAll() {
        List<Post> list = super.findAll();
        int length = list == null ? 0 : list.size();
        Random rand = new Random();

        for (int i = 0; i < length; i++) {
            Post p = list.get(i);
            p.setNumberOfComments(CommentService.getInstant().countByPostId(p.getId()));
            p.setNumberOfHearts(rand.nextLong(10000));
        }

        return list;
    }
}
