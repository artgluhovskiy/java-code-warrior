package org.art.web.warrior.tasking.service.api;

import org.art.web.warrior.tasking.model.CodingTask;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    CodingTask publishTask(CodingTask task);

    CodingTask updateTask(CodingTask task);

    Optional<CodingTask> getTaskByNameId(String nameId);

    List<CodingTask> getAllTasks();

    void deleteTaskByNameId(String nameId);
}
