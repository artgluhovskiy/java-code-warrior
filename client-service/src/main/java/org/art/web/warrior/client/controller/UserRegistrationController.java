package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.UserDto;
import org.art.web.warrior.client.exception.EmailExistsException;
import org.art.web.warrior.client.model.User;
import org.art.web.warrior.client.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static org.art.web.warrior.client.CommonServiceConstants.*;

@Slf4j
@Controller
@RequestMapping("user")
public class UserRegistrationController {

    private final UserService userService;

    @Autowired
    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("registration")
    public ModelAndView showRegistrationForm(ModelMap model) {
        model.addAttribute(VIEW_FRAGMENT, REGISTRATION_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
    }

    @PostMapping("registration")
    public ModelAndView registerUserAccount(@Valid @ModelAttribute(USER_ATTR_NAME) UserDto userDto, BindingResult result, ModelMap model) {
        User user = new User();
        if (!result.hasErrors()) {
            user = createUserAccount(userDto);
        }
        if (user == null) {
            result.rejectValue("email", "messages.regError");
        }
        if (result.hasErrors()) {
            model.addAttribute(VIEW_FRAGMENT, REGISTRATION_FRAGMENT);
            return new ModelAndView(LAYOUT_VIEW_NAME, model);
        } else {
            return new ModelAndView(REDIRECT + TASKS);
        }
    }

    @ModelAttribute(USER_ATTR_NAME)
    public UserDto user() {
        return new UserDto();
    }

    private User createUserAccount(UserDto userDto) {
        User registered;
        try {
            registered = userService.registerNewUserAccount(userDto);
        } catch (EmailExistsException e) {
            return null;
        }
        return registered;
    }
}
