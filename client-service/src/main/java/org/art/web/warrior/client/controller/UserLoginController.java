package org.art.web.warrior.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.art.web.warrior.client.CommonServiceConstants.LOGIN_VIEW_NAME;
import static org.art.web.warrior.client.CommonServiceConstants.TASKS_VIEW_NAME;

@Controller
@RequestMapping("user")
public class UserLoginController {

    @GetMapping("login")
    public String login() {
        return LOGIN_VIEW_NAME;
    }

    @PostMapping("login")
    public String login(@RequestParam("login") String login, @RequestParam("password") String password) {
        System.out.println(login);
        System.out.println(password);
        return TASKS_VIEW_NAME;
//        return TASKS_VIEW_NAME;
    }
}
