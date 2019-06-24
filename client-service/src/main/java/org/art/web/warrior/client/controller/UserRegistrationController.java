package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.service.client.api.UserServiceClient;
import org.art.web.warrior.client.util.ServiceResponseUtil;
import org.art.web.warrior.commons.users.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    private final UserServiceClient userServiceClient;

    @Autowired
    public UserRegistrationController(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @GetMapping("registration")
    public ModelAndView showRegistrationForm(ModelMap model) {
        model.addAttribute(VIEW_FRAGMENT, REGISTRATION_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
    }

    @PostMapping("registration")
    public ModelAndView registerUserAccount(@Valid @ModelAttribute(USER_ATTR_NAME) UserDto userDto, BindingResult result, ModelMap model) {
        if (!result.hasErrors()) {
            ResponseEntity<UserDto> userServiceResponse = userServiceClient.registerNewUserAccount(userDto);
            if (ServiceResponseUtil.isUserServiceErrorResponse(userServiceResponse)) {
                model.addAttribute(VIEW_FRAGMENT, REGISTRATION_FRAGMENT);
                return new ModelAndView(LAYOUT_VIEW_NAME, model);
            }
            return new ModelAndView(REDIRECT + TASKS);
        } else {
            model.addAttribute(VIEW_FRAGMENT, REGISTRATION_FRAGMENT);
            return new ModelAndView(LAYOUT_VIEW_NAME, model);
        }
    }

    @ModelAttribute(USER_ATTR_NAME)
    public UserDto user() {
        return new UserDto();
    }
}
