package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static org.art.web.warrior.client.CommonServiceConstants.*;

@Slf4j
@Controller
@RequestMapping(USER)
public class UserRegistrationController {

    @GetMapping(REGISTRATION)
    public ModelAndView showRegistrationForm(ModelMap model) {
        UserDto userDto = new UserDto();
        model.addAttribute(USER_ATTR_NAME, userDto);
        model.addAttribute(VIEW_FRAGMENT, REGISTRATION_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
    }

    @PostMapping(REGISTRATION)
    public ModelAndView registerUserAccount(@ModelAttribute(USER_ATTR_NAME) @Valid UserDto userDto, RedirectAttributes redirectAttrs) {
        log.info("User data: {}", userDto);
        redirectAttrs.addFlashAttribute(USER_ATTR_NAME, userDto);
        return new ModelAndView(REDIRECT + TASKS);
    }
}
