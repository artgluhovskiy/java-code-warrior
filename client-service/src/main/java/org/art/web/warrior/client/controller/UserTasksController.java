package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.UserDto;
import org.art.web.warrior.client.model.User;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.commons.tasking.dto.CodingTaskDescriptor;
import org.art.web.warrior.commons.tasking.dto.CodingTaskDescriptorsResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.art.web.warrior.client.CommonServiceConstants.*;

@Slf4j
@Controller
@RequestMapping(USER)
public class UserTasksController {

    private final TaskServiceClient taskServiceClient;

    @Autowired
    public UserTasksController(TaskServiceClient taskServiceClient) {
        this.taskServiceClient = taskServiceClient;
    }

    @GetMapping(TASKS)
    public ModelAndView showTasksPage(@ModelAttribute(USER_ATTR_NAME) User user, ModelMap model) {
        log.info("User Data Tasks Controller: {}", user);
        CodingTaskDescriptorsResp taskServiceResponse = taskServiceClient.getCodingTaskDescriptors();
        List<CodingTaskDescriptor> codingTasks = taskServiceResponse.getCodingTasks();

        model.addAttribute(VIEW_FRAGMENT, TASKS_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
    }
}
