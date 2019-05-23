package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.art.web.warrior.client.CommonServiceConstants.*;

@Slf4j
@Controller
@RequestMapping(USER)
public class UserTasksController {

    @GetMapping(TASKS)
    public ModelAndView showTasksPage(@ModelAttribute(USER_ATTR_NAME) UserDto userDto, ModelMap model) {
        log.info("User Data Tasks Controller: {}", userDto);
        model.addAttribute(VIEW_FRAGMENT, TASKS_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
    }
}
