package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.UserCodingTaskDto;
import org.art.web.warrior.client.dto.UserDto;
import org.art.web.warrior.client.model.User;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.service.api.UserService;
import org.art.web.warrior.commons.tasking.dto.CodingTaskDescriptor;
import org.art.web.warrior.commons.tasking.dto.CodingTaskDescriptorsResp;
import org.art.web.warrior.commons.tasking.dto.TaskServiceResp;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("user")
public class UserTasksController {

    private final UserService userService;

    private final TaskServiceClient taskServiceClient;

    @Autowired
    public UserTasksController(UserService userService, TaskServiceClient taskServiceClient) {
        this.userService = userService;
        this.taskServiceClient = taskServiceClient;
    }

    @GetMapping("tasks")
    public ModelAndView showTasksPage(ModelMap model) {
        UserDto user = (UserDto) model.get(USER_ATTR_NAME);
        log.debug("Task list page request. User: {}", user.getEmail());
        CodingTaskDescriptorsResp taskServiceResponse = taskServiceClient.getCodingTaskDescriptors();
        List<CodingTaskDescriptor> taskDescriptors = taskServiceResponse.getCodingTasks();
        List<UserCodingTaskDto> userCodingTaskList = mapToUserCodingTaskDto(user.getSolvedTaskNameIds(), taskDescriptors);
        model.addAttribute(USER_TASK_LIST_ATTR_NAME, userCodingTaskList);
        model.addAttribute(VIEW_FRAGMENT, TASKS_FRAGMENT);
        return new ModelAndView(LAYOUT_VIEW_NAME, model);
    }

    @GetMapping("task/{taskNameId}")
    public ModelAndView taskPage(@PathVariable("taskNameId") String taskNameId, ModelMap model) {
        log.debug("Task page request for the task with ID: {}", taskNameId);
        TaskServiceResp taskServiceResp = taskServiceClient.getCodingTaskByNameId(taskNameId);
        model.addAttribute(TASK_ATTR_NAME, taskServiceResp);
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

    private List<UserCodingTaskDto> mapToUserCodingTaskDto(Set<String> solvedUserTasks, List<CodingTaskDescriptor> taskDescriptors) {
        return taskDescriptors.stream()
            .map(desc -> {
                UserCodingTaskDto codingTaskDto = new UserCodingTaskDto(desc.getNameId(), desc.getName(), desc.getDescription());
                if (solvedUserTasks.contains(desc.getNameId())) {
                    codingTaskDto.setSolved(true);
                }
                return codingTaskDto;
            }).collect(Collectors.toList());
    }
}
