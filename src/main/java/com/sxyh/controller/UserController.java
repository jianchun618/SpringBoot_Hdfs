package com.sxyh.controller;

import com.sxyh.entity.User;
import com.sxyh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;


/*
 * 组合注解@RestController=@Controller+ @ResponseBody
 * 加在控制器类上表明当前类是控制器对象，并且返回json
 *
 * springboot默认的视图层模板不是jsp  thymeleaf
 * */
@Controller
@RequestMapping("/User")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/queryAll")
    @ResponseBody
    public List<User> queryAll(HttpSession session) {
        List<User> users = userService.queryAll();
        //session.setAttribute("users",users);
        return users;
    }

    @RequestMapping("/save")
    public void save() {
        User us = new User(1, "zhangsan", "002222");
        userService.save(us);
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

}
