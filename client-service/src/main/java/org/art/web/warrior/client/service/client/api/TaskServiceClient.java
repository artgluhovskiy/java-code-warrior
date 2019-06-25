package org.art.web.warrior.client.service.client.api;

import org.art.web.warrior.commons.tasking.dto.TaskDescriptorDto;
import org.art.web.warrior.commons.tasking.dto.TaskDto;

import java.util.List;

public interface TaskServiceClient {

    TaskDescriptorDto publishCodingTask(TaskDto task);

    TaskDto getCodingTaskByNameId(String nameId);

    TaskDescriptorDto updateCodingTask(TaskDto task);

    List<TaskDto> getAllTasks();

    List<TaskDescriptorDto> getCodingTaskDescriptors();

    void deleteTask(String nameId);
}
