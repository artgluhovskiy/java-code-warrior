package org.art.web.warrior.tasking.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.commons.tasking.dto.TaskDescriptorDto;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.art.web.warrior.tasking.exception.TaskNotFoundException;
import org.art.web.warrior.tasking.model.CodingTask;
import org.art.web.warrior.tasking.service.api.TaskService;
import org.art.web.warrior.tasking.util.ServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.art.web.warrior.commons.CommonConstants.KRYO_CONTENT_TYPE;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskServiceController {

    private final TaskService taskService;

    @Autowired
    public TaskServiceController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(consumes = KRYO_CONTENT_TYPE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskDescriptorDto publishTask(@Valid @RequestBody TaskDto taskDto) {
        log.debug("Making a publishing request for a new coding task. Task data: {}", taskDto);
        CodingTask codingTask = ServiceMapper.mapToCodingTask(taskDto);
        codingTask = taskService.publishTask(codingTask);
        return ServiceMapper.mapToTaskDescriptorDto(codingTask.getDescriptor());
    }

    @PutMapping(consumes = KRYO_CONTENT_TYPE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskDescriptorDto updateTask(@Valid @RequestBody TaskDto taskDto) {
        log.debug("Making an update request for the coding task. Task data: {}", taskDto);
        CodingTask codingTask = ServiceMapper.mapToCodingTask(taskDto);
        codingTask = taskService.updateTask(codingTask);
        return ServiceMapper.mapToTaskDescriptorDto(codingTask.getDescriptor());
    }

    @GetMapping(value = "/{nameId}", produces = KRYO_CONTENT_TYPE)
    public TaskDto getTaskByNameId(@PathVariable String nameId) {
        log.debug("Making the request for the coding task by its name id. Task name id: {}", nameId);
        CodingTask codingTask = taskService.getTaskByNameId(nameId)
                .orElseThrow(() -> new TaskNotFoundException("Cannot find a coding task with such name ID!", nameId));
        return ServiceMapper.mapToTaskDto(codingTask);
    }

    @GetMapping(produces = KRYO_CONTENT_TYPE)
    public List<TaskDto> getAllTasks() {
        log.debug("Making the request for all coding tasks");
        List<CodingTask> codingTasks = taskService.getAllTasks();
        return codingTasks.stream()
                .map(ServiceMapper::mapToTaskDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/descriptors", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TaskDescriptorDto> getAllTaskDescriptors() {
        log.debug("Making the request for all coding task descriptors");
        List<CodingTask> codingTasks = taskService.getAllTasks();
        return codingTasks.stream()
                .map(CodingTask::getDescriptor)
                .map(ServiceMapper::mapToTaskDescriptorDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{nameId}")
    public void deleteTaskById(@PathVariable String nameId) {
        log.debug("Making the request for coding task deletion. Task name id: {}", nameId);
        taskService.getTaskByNameId(nameId);
    }

    @GetMapping("/ping")
    public String ping() {
        return "Task Service: OK!";
    }
}
