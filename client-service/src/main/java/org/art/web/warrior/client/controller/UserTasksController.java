package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.UserCodingTaskDto;
import org.art.web.warrior.client.dto.UserDto;
import org.art.web.warrior.client.model.User;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.service.api.UserService;
import org.art.web.warrior.client.util.ServiceResponseUtil;
import org.art.web.warrior.commons.tasking.dto.TaskDescriptorDto;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.art.web.warrior.client.CommonServiceConstants.*;

@Slf4j
@Controller
@SessionAttributes(USER_ATTR_NAME)
@RequestMapping("/user/tasks")
public class UserTasksController {

    private final UserService userService;

    private final TaskServiceClient taskServiceClient;

    @Autowired
    public UserTasksController(UserService userService, TaskServiceClient taskServiceClient) {
        this.userService = userService;
        this.taskServiceClient = taskServiceClient;
    }

    @GetMapping
    public ModelAndView showTasksPage(ModelMap model) {
        UserDto user = (UserDto) model.get(USER_ATTR_NAME);
        log.debug("Task list page request. User: {}", user.getEmail());
        List<TaskDescriptorDto> taskDescriptors;
        ResponseEntity<List<TaskDescriptorDto>> taskServiceResponse = taskServiceClient.getCodingTaskDescriptors();
        if (!ServiceResponseUtil.isTaskServiceErrorResponse(taskServiceResponse)) {
            taskDescriptors = taskServiceResponse.getBody();
        } else {
            taskDescriptors = Collections.emptyList();
        }
        List<UserCodingTaskDto> userCodingTaskList = mapToUserCodingTaskDto(user.getSolvedTaskNameIds(), taskDescriptors);
        model.addAttribute(USER_TASK_LIST_ATTR_NAME, userCodingTaskList);
        model.addAttribute(VIEW_FRAGMENT, TASKS_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
    }

    @GetMapping("/{taskNameId}")
    public ModelAndView taskPage(@PathVariable String taskNameId, ModelMap model) {
        log.debug("Task page request for the task with ID: {}", taskNameId);
        ResponseEntity<TaskDto> taskServiceResponse = taskServiceClient.getCodingTaskByNameId(taskNameId);
        TaskDto task;
        if (!ServiceResponseUtil.isTaskServiceErrorResponse(taskServiceResponse)) {
            task = taskServiceResponse.getBody();
        } else {
            task = new TaskDto();
        }
        model.addAttribute(TASK_ATTR_NAME, task);
        model.addAttribute(VIEW_FRAGMENT, SUBMISSION_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
    }

    @ModelAttribute
    public void initUser(Principal principal, ModelMap model) {
        UserDto userDto = (UserDto) model.get(USER_ATTR_NAME);
        if (userDto == null) {
            String userName = principal.getName();
            User user = userService.findUserByEmail(userName);
            userDto = populateUserDto(user);
            model.addAttribute(USER_ATTR_NAME, userDto);
        }
    }

    private UserDto populateUserDto(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .solvedTaskNameIds(user.getSolvedTaskNameIds())
                .build();
    }

    private List<UserCodingTaskDto> mapToUserCodingTaskDto(Set<String> solvedUserTasks, List<TaskDescriptorDto> taskDescriptors) {
        return taskDescriptors.stream()
                .map(desc -> {
                    UserCodingTaskDto codingTaskDto = new UserCodingTaskDto(desc.getNameId(), desc.getName(), desc.getDescription(), desc.getRating());
                    if (solvedUserTasks.contains(desc.getNameId())) {
                        codingTaskDto.setSolved(true);
                    }
                    return codingTaskDto;
                }).collect(Collectors.toList());
    }
}
