package com.sxyh.dao;

import com.sxyh.entity.User;

import java.util.List;

public interface UserMapper {
    List<User> queryAll();
    void save(User user);
}
