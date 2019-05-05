package org.art.web.warrior.tasking.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.commons.tasking.dto.TaskServicePubRequest;
import org.art.web.warrior.commons.tasking.dto.TaskServicePubResponse;
import org.art.web.warrior.tasking.model.CodingTask;
import org.art.web.warrior.tasking.service.api.TaskService;
import org.art.web.warrior.tasking.util.ServiceRequestUtil;
import org.art.web.warrior.tasking.util.ServiceResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(value = "/publish", consumes = KRYO_CONTENT_TYPE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskServicePubResponse publishNewCodingTask(@RequestBody TaskServicePubRequest requestData) {
        CodingTask codingTask = ServiceRequestUtil.buildCodingTaskFromRequest(requestData);
        codingTask = this.taskService.publishTask(codingTask);
        return ServiceResponseUtil.buildTaskPubResponse(codingTask);
    }

    @GetMapping("/ping")
    public String ping() {
        return TASK_SERVICE_OK_MESSAGE;
    }
}
