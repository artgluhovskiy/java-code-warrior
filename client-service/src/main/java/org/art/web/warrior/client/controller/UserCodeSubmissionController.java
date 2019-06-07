package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.dto.UserDto;
import org.art.web.warrior.client.dto.UserTaskCodeData;
import org.art.web.warrior.client.model.User;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.client.service.api.ExecServiceClient;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.service.api.UserService;
import org.art.web.warrior.client.util.ClientRequestUtil;
import org.art.web.warrior.client.util.ClientsResponseUtil;
import org.art.web.warrior.client.util.ClientResponseUtil;
import org.art.web.warrior.commons.CommonConstants;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitDto;
import org.art.web.warrior.commons.execution.dto.ExecutionRequest;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.art.web.warrior.commons.tasking.dto.TaskServiceResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.util.Collections.singletonList;
import static org.art.web.warrior.client.CommonServiceConstants.USER_ATTR_NAME;

@Slf4j
@RestController
@SessionAttributes(USER_ATTR_NAME)
@RequestMapping(
    value = "/user",
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

    @PostMapping("/submit")
    public ClientServiceResponse submitClientCode(@Valid @RequestBody UserTaskCodeData userTaskData,
                                                  @ModelAttribute(USER_ATTR_NAME) UserDto user) {
        String className = userTaskData.getClassName();
        String srcCode = userTaskData.getSrcCode();
        String taskNameId = userTaskData.getTaskNameId();
        log.debug("Client code submission request: class name {}, source code {}, task name id {}", className, srcCode, taskNameId);
        CompilationUnitDto compilationData = new CompilationUnitDto(className, srcCode);
        CompilationRequest compRequest = new CompilationRequest(singletonList(compilationData));
        ResponseEntity<CompilationResponse> compServiceResponse = compServiceClient.compileSrc(compRequest);
        if (!ClientResponseUtil.isCompServiceOkResponse(compServiceResponse)) {
            return ClientResponseUtil.buildCompServiceErrorResponse(compServiceResponse);
        }
        ResponseEntity<TaskDto> taskServiceResponse = taskServiceClient.getCodingTaskByNameId(taskNameId);
        if (!ClientResponseUtil.isTaskServiceOkResponse(taskServiceResponse)) {
            return ClientResponseUtil.buildTaskServiceErrorResp(taskServiceResponse);
        }
        CompilationUnitDto compiledSolutionData = compServiceResponse.getBody().getCompUnitResults().get(CommonConstants.SOLUTION_CLASS_NAME);
        TaskDto taskDto = taskServiceResponse.getBody();
        ExecutionRequest executionRequest = ClientRequestUtil.buildExecutionServiceRequest(compiledSolutionData, taskDto);
        ResponseEntity<ExecutionResponse> execServiceResponse = execServiceClient.executeCode(executionRequest);
        if (!ClientResponseUtil.isExecServiceOkResponse(execServiceResponse)) {
            return ClientResponseUtil.buildExecServiceErrorResp(execServiceResponse);
        }
        updateUserTaskList(user, taskNameId);
        return ClientsResponseUtil.buildUserTaskExecutionResp(userTaskData, execServiceResp);
    }

    private void updateUserTaskList(UserDto userDto, String taskNameId) {
        User user = userService.findUserByEmail(userDto.getEmail());
        user.getSolvedTaskNameIds().add(taskNameId);
        userService.updateUser(user);
        userDto.getSolvedTaskNameIds().add(taskNameId);
    }
}