package org.art.web.warrior.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static org.art.web.warrior.client.CommonServiceConstants.*;

@Controller
@RequestMapping(USER)
public class UserLoginController {

    @GetMapping(LOGIN)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView(LAYOUT_VIEW_NAME);
        modelAndView.addObject(FRAGMENT, LOGIN_FRAGMENT);
        return modelAndView;
    }

    @PostMapping(LOGIN)
    public String login(@RequestParam("login") String login, @RequestParam("password") String password) {
        //TODO: Implement login logic
        return LAYOUT_VIEW_NAME;
    }
}
