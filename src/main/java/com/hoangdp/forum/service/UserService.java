package com.hoangdp.forum.service;

import java.util.UUID;

import com.hoangdp.forum.entity.User;

public class UserService extends Service<User, UUID> {

    public UserService() {
        super(User.class);
    }

}
