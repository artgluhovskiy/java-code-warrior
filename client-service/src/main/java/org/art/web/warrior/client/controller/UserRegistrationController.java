package org.art.web.warrior.client.controller;

import org.art.web.warrior.client.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import static org.art.web.warrior.client.CommonServiceConstants.REGISTRATION_VIEW_NAME;

@Controller
@RequestMapping("user")
public class UserRegistrationController {

    @GetMapping(value = "registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return REGISTRATION_VIEW_NAME;
    }
}
