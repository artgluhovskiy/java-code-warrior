package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.commons.users.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.art.web.warrior.client.CommonServiceConstants.*;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserLoginController {

    @GetMapping("/login")
    public ModelAndView userLogin(ModelMap model) {
        log.debug("User login page request.");
        model.addAttribute(VIEW_FRAGMENT, LOGIN_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
    }

    @ModelAttribute(USER_ATTR_NAME)
    public UserDto user() {
        return new UserDto();
    }
}
