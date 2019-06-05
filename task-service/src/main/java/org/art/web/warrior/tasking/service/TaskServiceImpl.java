package org.art.web.warrior.tasking.service;

import org.art.web.warrior.tasking.model.CodingTask;
import org.art.web.warrior.tasking.repository.CodingTaskRepository;
import org.art.web.warrior.tasking.service.api.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final CodingTaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(CodingTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public CodingTask publishTask(CodingTask task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<CodingTask> getTaskByNameId(String nameId) {
        return taskRepository.getCodingTaskByNameId(nameId);
    }

    @Override
    public List<CodingTask> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public void deleteTaskByNameId(String nameId) {
        taskRepository.deleteByNameId(nameId);
    }
}
