package org.art.web.warrior.tasking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.art.web.warrior.tasking.ServiceCommonConstants.TASK_SERVICE_OK_MESSAGE;

@Slf4j
@RestController
public class TaskServiceController {

    @GetMapping("/ping")
    public String ping() {
        return TASK_SERVICE_OK_MESSAGE;
    }

    public String publishNewCodingTask() {

        return null;
    }


}
