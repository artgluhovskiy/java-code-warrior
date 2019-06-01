package org.art.web.warrior.tasking.service;

import org.art.web.warrior.tasking.model.CodingTask;
import org.art.web.warrior.tasking.repository.CodingTaskRepository;
import org.art.web.warrior.tasking.service.api.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final CodingTaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(CodingTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public CodingTask publishTask(CodingTask task) {
        return this.taskRepository.save(task);
    }

    @Override
    public CodingTask getTaskByNameId(String nameId) {
        return this.taskRepository.getCodingTaskByNameId(nameId);
    }

    @Override
    public List<CodingTask> getCodingTasks() {
        return this.taskRepository.findAll();
    }
}
