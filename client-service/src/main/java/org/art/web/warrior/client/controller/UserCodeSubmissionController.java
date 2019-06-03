package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.ClientServiceUserResp;
import org.art.web.warrior.client.dto.UserDto;
import org.art.web.warrior.client.dto.UserTaskCodeData;
import org.art.web.warrior.client.model.User;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.client.service.api.ExecServiceClient;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.service.api.UserService;
import org.art.web.warrior.client.util.ClientRequestUtil;
import org.art.web.warrior.client.util.ClientResponseUtil;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompServiceReq;
import org.art.web.warrior.commons.compiler.dto.CompServiceResp;
import org.art.web.warrior.commons.compiler.dto.CompilationUnit;
import org.art.web.warrior.commons.execution.dto.ExecutionReq;
import org.art.web.warrior.commons.execution.dto.ExecutionResp;
import org.art.web.warrior.commons.tasking.dto.TaskServiceResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static java.util.Collections.singletonList;
import static org.art.web.warrior.client.CommonServiceConstants.USER_ATTR_NAME;

@Slf4j
@RestController
@SessionAttributes(USER_ATTR_NAME)
@RequestMapping(
    value = "user",
    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
public class UserCodeSubmissionController {

    private final CompServiceClient compServiceClient;

    private final ExecServiceClient execServiceClient;

    private final TaskServiceClient taskServiceClient;

    private final UserService userService;

    @Autowired
    public UserCodeSubmissionController(CompServiceClient compServiceClient, ExecServiceClient execServiceClient,
                                        TaskServiceClient taskServiceClient, UserService userService) {
        this.compServiceClient = compServiceClient;
        this.execServiceClient = execServiceClient;
        this.taskServiceClient = taskServiceClient;
        this.userService = userService;
    }

    @PostMapping("submit")
    public ClientServiceUserResp submitClientCode(@Valid @RequestBody UserTaskCodeData userTaskData,
                                                  @ModelAttribute(USER_ATTR_NAME) UserDto user, HttpSession session) {
        String className = userTaskData.getClassName();
        String srcCode = userTaskData.getSrcCode();
        String taskNameId = userTaskData.getTaskNameId();
        log.debug("Client code submission request: class name {}, source code {}, task name id {}", className, srcCode, taskNameId);
        CompilationUnit requestCompData = new CompilationUnit(className, srcCode);
        CompServiceReq compRequest = new CompServiceReq(singletonList(requestCompData));
        CompServiceResp compServiceResp = compServiceClient.compileSrc(compRequest);
        if (compServiceResp == null) {
            log.debug("Internal service error occurred! Compilation service responded with empty body.");
            return ClientResponseUtil.buildUserTaskEmptyBodyResp(userTaskData);
        }
        if (compServiceResp.isCompError()) {
            log.debug("Compilation errors occurred while compiling client source code!");
            return ClientResponseUtil.buildUserTaskCompilationErrorResp(userTaskData, compServiceResp);
        }
        TaskServiceResp taskServiceResp = this.taskServiceClient.getCodingTaskByNameId(taskNameId);
        if (!ServiceResponseStatus.SUCCESS.getStatusId().equals(taskServiceResp.getRespStatus())) {
            return ClientResponseUtil.buildUserTaskServiceErrorResp(userTaskData, taskServiceResp);
        }
        ExecutionReq executionReq = ClientRequestUtil.buildExecutionServiceRequest(compServiceResp, taskServiceResp);
        ExecutionResp execServiceResp = this.execServiceClient.executeCode(executionReq);
        if (ServiceResponseStatus.SUCCESS.getStatusId().equals(execServiceResp.getRespStatus())) {
            updateUserTaskList(user, userTaskData.getTaskNameId());
        }
        return ClientResponseUtil.buildUserTaskExecutionResp(userTaskData, execServiceResp);
    }

    private void updateUserTaskList(UserDto userDto, String taskNameId) {
        User user = userService.findUserByEmail(userDto.getEmail());
        user.getSolvedTaskNameIds().add(taskNameId);
        userService.updateUser(user);
        userDto.getSolvedTaskNameIds().add(taskNameId);
    }
}