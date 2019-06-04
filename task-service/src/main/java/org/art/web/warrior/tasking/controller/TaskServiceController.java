package org.art.web.warrior.tasking.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.commons.tasking.dto.*;
import org.art.web.warrior.tasking.model.CodingTask;
import org.art.web.warrior.tasking.model.CodingTaskDescriptor;
import org.art.web.warrior.tasking.service.api.TaskService;
import org.art.web.warrior.tasking.util.ServiceRequestUtil;
import org.art.web.warrior.tasking.util.ServiceResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.art.web.warrior.commons.CommonConstants.KRYO_CONTENT_TYPE;
import static org.art.web.warrior.tasking.ServiceCommonConstants.TASK_SERVICE_OK_MESSAGE;

@Slf4j
@RestController
public class TaskServiceController {

    private final TaskService taskService;

    @Autowired
    public TaskServiceController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(value = "/tasks", consumes = KRYO_CONTENT_TYPE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  publishTask(@Valid @RequestBody TaskDto requestData) {
        CodingTask codingTask = ServiceRequestUtil.buildCodingTaskFromReqData(requestData);
        codingTask = this.taskService.publishTask(codingTask);
        return ServiceResponseUtil.buildCodingTaskPublicationResp(codingTask);
    }

    @GetMapping(value = "/tasks/{nameId}", produces = KRYO_CONTENT_TYPE)
    public TaskServiceResp getTaskByNameId(@PathVariable String nameId) {
        CodingTask codingTask = this.taskService.getTaskByNameId(nameId);
        return ServiceResponseUtil.buildCodingTaskResp(codingTask);
    }

    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<CodingTask> getAllTasks() {
        return this.taskService.getAllTasks();
    }

    @GetMapping(value = "/tasks/descriptors", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<CodingTaskDescriptor> getCodingTaskDescriptors() {
        List<CodingTask> codingTasks = this.taskService.getAllTasks();
        return ServiceResponseUtil.buildCodingTaskDescriptorsResp(codingTasks);
    }

    @DeleteMapping("/tasks/{nameId}")
    public void deleteTaskById(@PathVariable String nameId) {
        taskService.getTaskByNameId(nameId);
    }

    @GetMapping("/ping")
    public String ping() {
        return TASK_SERVICE_OK_MESSAGE;
    }
}
