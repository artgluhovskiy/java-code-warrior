package org.art.web.warrior.tasking.service.api;

import org.art.web.warrior.tasking.model.CodingTask;

import java.util.List;

public interface TaskService {

    CodingTask publishTask(CodingTask task);

    CodingTask getTaskByNameId(String nameId);

    List<CodingTask> getAllTasks();
}
