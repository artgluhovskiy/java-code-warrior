package org.art.web.warrior.client.controller;

import org.art.web.warrior.client.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.art.web.warrior.client.CommonServiceConstants.*;

@Controller
@RequestMapping(USER)
public class UserRegistrationController {

    @GetMapping(REGISTRATION)
    public ModelAndView showRegistrationForm() {
        ModelAndView modelAndView = new ModelAndView(LAYOUT_VIEW_NAME);
        UserDto userDto = new UserDto();
        modelAndView.addObject(USER_ATTR_NAME, userDto);
        modelAndView.addObject(FRAGMENT, REGISTRATION_FRAGMENT);
        return modelAndView;
    }
}
