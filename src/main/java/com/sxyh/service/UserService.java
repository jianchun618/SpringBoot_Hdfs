package com.sxyh.service;

import com.sxyh.entity.User;

import java.util.List;

public interface UserService {
    public List<User> queryAll();

    public void save(User user);
}
