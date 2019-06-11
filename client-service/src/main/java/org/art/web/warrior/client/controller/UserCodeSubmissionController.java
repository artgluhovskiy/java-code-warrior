package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.dto.UserDto;
import org.art.web.warrior.client.dto.UserTaskDto;
import org.art.web.warrior.client.model.User;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.client.service.api.ExecServiceClient;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.service.api.UserService;
import org.art.web.warrior.client.util.ServiceRequestUtil;
import org.art.web.warrior.client.util.ServiceResponseUtil;
import org.art.web.warrior.commons.CommonConstants;
import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitDto;
import org.art.web.warrior.commons.execution.dto.ExecutionRequest;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;
import org.art.web.warrior.commons.tasking.dto.TaskDescriptorDto;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ClientServiceResponse submitClientCode(@Valid @RequestBody UserTaskDto userTaskData,
                                                  @ModelAttribute(USER_ATTR_NAME) UserDto user) {
        String className = userTaskData.getClassName();
        String srcCode = userTaskData.getSrcCode();
        String taskNameId = userTaskData.getTaskNameId();
        log.debug("Client code submission request: class name {}, source code {}, task name id {}", className, srcCode, taskNameId);
        CompilationRequest compRequest = ServiceRequestUtil.buildCompilationServiceRequest(className, srcCode);
        ResponseEntity<CompilationResponse> compServiceResponse = compServiceClient.compileSrc(compRequest);
        if (ServiceResponseUtil.isCompServiceErrorResponse(compServiceResponse)) {
            return ServiceResponseUtil.buildCompServiceErrorResponse(compServiceResponse);
        }
        ResponseEntity<TaskDto> taskServiceResponse = taskServiceClient.getCodingTaskByNameId(taskNameId);
        if (ServiceResponseUtil.isTaskServiceErrorResponse(taskServiceResponse)) {
            return ServiceResponseUtil.buildTaskServiceErrorResp(taskServiceResponse);
        }
        CompilationUnitDto compiledSolutionData = compServiceResponse.getBody().getCompUnitResults().get(CommonConstants.SOLUTION_CLASS_NAME);
        TaskDto taskDto = taskServiceResponse.getBody();
        ExecutionRequest executionRequest = ServiceRequestUtil.buildExecutionServiceRequest(compiledSolutionData.getCompiledClassBytes(), taskDto.getRunnerClassData());
        ResponseEntity<ExecutionResponse> execServiceResponse = execServiceClient.executeCode(executionRequest);
        if (ServiceResponseUtil.isExecServiceErrorResponse(execServiceResponse)) {
            return ServiceResponseUtil.buildExecServiceErrorResp(execServiceResponse);
        }
        updateUserTaskList(user, taskNameId);
        increaseCodingTaskRating(taskDto);
        return ServiceResponseUtil.buildUserTaskExecutionResponse(execServiceResponse.getBody());
    }

    private void updateUserTaskList(UserDto userDto, String taskNameId) {
        User user = userService.findUserByEmail(userDto.getEmail());
        user.getSolvedTaskNameIds().add(taskNameId);
        userService.updateUser(user);
        userDto.getSolvedTaskNameIds().add(taskNameId);
    }

    private void increaseCodingTaskRating(TaskDto task) {
        TaskDescriptorDto taskDescriptor = task.getDescriptor();
        task.getDescriptor().setRating(taskDescriptor.getRating() + 1);
        taskServiceClient.updateCodingTask(task);
    }
}