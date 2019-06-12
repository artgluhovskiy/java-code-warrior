package org.art.web.warrior.client.service.client.api;

import org.art.web.warrior.commons.tasking.dto.TaskDescriptorDto;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskServiceClient {

    ResponseEntity<TaskDescriptorDto> publishCodingTask(TaskDto task);

    ResponseEntity<TaskDto> getCodingTaskByNameId(String nameId);

    ResponseEntity<TaskDescriptorDto> updateCodingTask(TaskDto task);

    ResponseEntity<List<TaskDto>> getAllTasks();

    ResponseEntity<List<TaskDescriptorDto>> getCodingTaskDescriptors();

    void deleteTask(String nameId);
}
