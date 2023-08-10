package com.hoangdp.forum.service;

import com.hoangdp.forum.entity.Post;

public class PostService extends Service<Post, Long> {

    public PostService() {
        super(Post.class);
    }

}
