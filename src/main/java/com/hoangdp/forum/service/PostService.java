package com.hoangdp.forum.service;

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
}
