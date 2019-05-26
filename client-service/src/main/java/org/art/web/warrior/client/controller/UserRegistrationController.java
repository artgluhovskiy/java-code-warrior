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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static org.art.web.warrior.client.CommonServiceConstants.*;

@Slf4j
@Controller
@RequestMapping(USER)
public class UserRegistrationController {

    private final UserService userService;

    @Autowired
    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(REGISTRATION)
    public ModelAndView showRegistrationForm(ModelMap model) {
        UserDto userDto = new UserDto();
        model.addAttribute(USER_ATTR_NAME, userDto);
        model.addAttribute(VIEW_FRAGMENT, REGISTRATION_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
    }

    @PostMapping(REGISTRATION)
    public ModelAndView registerUserAccount(@ModelAttribute(USER_ATTR_NAME) @Valid UserDto userDto,
                                            BindingResult result, RedirectAttributes redirectAttrs) {
        User registered = new User();
        if (!result.hasErrors()) {
            registered = createUserAccount(userDto);
        }
        if (registered == null) {
            result.rejectValue("email", "message.regError");
        }
        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            model.addAttribute(USER_ATTR_NAME, userDto);
            model.addAttribute(VIEW_FRAGMENT, REGISTRATION_FRAGMENT);
            return new ModelAndView(LAYOUT_VIEW_NAME, model);
        } else {
            redirectAttrs.addFlashAttribute(USER_ATTR_NAME, userDto);
            return new ModelAndView(REDIRECT + TASKS);
        }
    }

    @GetMapping(LOGIN)
    public ModelAndView userLogin(ModelMap model) {
        model.addAttribute(VIEW_FRAGMENT, LOGIN_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
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
