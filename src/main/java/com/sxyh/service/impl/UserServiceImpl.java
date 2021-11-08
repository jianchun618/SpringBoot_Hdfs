package com.sxyh.service.impl;

import com.sxyh.dao.UserMapper;
import com.sxyh.entity.User;
import com.sxyh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> queryAll() {
        List<User> users = userMapper.queryAll();
        return users;
    }

    @Override
    public void save(User user) {
        userMapper.save(user);
    }
}
