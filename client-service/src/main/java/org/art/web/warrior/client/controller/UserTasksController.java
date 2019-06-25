package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.UserCodingTaskDto;
import org.art.web.warrior.client.exception.ExternalServiceInvocationException;
import org.art.web.warrior.client.service.client.api.TaskServiceClient;
import org.art.web.warrior.client.service.client.api.UserServiceClient;
import org.art.web.warrior.client.util.ServiceResponseUtil;
import org.art.web.warrior.commons.tasking.dto.TaskDescriptorDto;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.art.web.warrior.commons.users.dto.TaskOrderDto;
import org.art.web.warrior.commons.users.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.art.web.warrior.client.CommonServiceConstants.*;

@Slf4j
@Controller
@SessionAttributes(USER_ATTR_NAME)
@RequestMapping("/user/tasks")
public class UserTasksController {

    private final UserServiceClient userServiceClient;

    private final TaskServiceClient taskServiceClient;

    @Autowired
    public UserTasksController(UserServiceClient userServiceClient, TaskServiceClient taskServiceClient) {
        this.userServiceClient = userServiceClient;
        this.taskServiceClient = taskServiceClient;
    }

    @GetMapping
    public ModelAndView showTasksPage(ModelMap model) {
        UserDto userDto = (UserDto) model.get(USER_ATTR_NAME);
        log.debug("Task list page request. User: {}", userDto.getEmail());
        List<TaskDescriptorDto> taskDescriptors;
        try {
            taskDescriptors = taskServiceClient.getCodingTaskDescriptors();
        } catch (ExternalServiceInvocationException e) {
            model.addAttribute(API_ERROR_ATTR_NAME, e.getApiError());
            model.addAttribute(VIEW_FRAGMENT, TASKS_FRAGMENT);
            return new ModelAndView(LAYOUT_VIEW_NAME, model);
        }
        List<UserCodingTaskDto> userCodingTaskList = mapToUserCodingTaskDto(userDto.getTaskOrders(), taskDescriptors);
        model.addAttribute(USER_TASK_LIST_ATTR_NAME, userCodingTaskList);
        model.addAttribute(VIEW_FRAGMENT, TASKS_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
    }

    @GetMapping("/{taskNameId}")
    public ModelAndView taskPage(@PathVariable String taskNameId, ModelMap model) {
        log.debug("Task page request for the task with ID: {}", taskNameId);
        TaskDto codingTask;
        try {
            codingTask = taskServiceClient.getCodingTaskByNameId(taskNameId);
        } catch (ExternalServiceInvocationException e) {
            model.addAttribute(API_ERROR_ATTR_NAME, e.getApiError());
            model.addAttribute(VIEW_FRAGMENT, SUBMISSION_FRAGMENT);
            return new ModelAndView(LAYOUT_VIEW_NAME, model);
        }
        model.addAttribute(TASK_ATTR_NAME, codingTask);
        model.addAttribute(VIEW_FRAGMENT, SUBMISSION_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
    }

    @ModelAttribute
    public void initUser(Principal principal, ModelMap model) {
        UserDto userDto = (UserDto) model.get(USER_ATTR_NAME);
        if (userDto == null) {
            String userName = principal.getName();
            try {
                userDto = userServiceClient.findUserByEmail(userName);
            } catch (ExternalServiceInvocationException e) {
                model.addAttribute(USER_ATTR_NAME, new UserDto());
            }
            model.addAttribute(USER_ATTR_NAME, userDto);
        }
    }

    private List<UserCodingTaskDto> mapToUserCodingTaskDto(Set<TaskOrderDto> solvedUserTasks, List<TaskDescriptorDto> taskDescriptors) {
        Set<String> solvedTaskIds = solvedUserTasks.stream()
            .map(TaskOrderDto::getNameId)
            .collect(Collectors.toSet());
        return taskDescriptors.stream()
            .map(desc -> {
                UserCodingTaskDto codingTaskDto = new UserCodingTaskDto(desc.getNameId(), desc.getName(), desc.getDescription(), desc.getRating());
                if (solvedTaskIds.contains(desc.getNameId())) {
                    codingTaskDto.setSolved(true);
                }
                return codingTaskDto;
            }).collect(Collectors.toList());
    }
}
