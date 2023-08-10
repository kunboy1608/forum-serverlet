package com.hoangdp.forum.service;

import com.hoangdp.forum.entity.Comment;

class CommentService extends Service<Comment, Long> {

    public CommentService() {
        super(Comment.class);
    }

}