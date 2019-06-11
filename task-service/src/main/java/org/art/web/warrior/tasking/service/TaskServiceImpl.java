package org.art.web.warrior.tasking.service;

import org.art.web.warrior.tasking.exception.TaskNotFoundException;
import org.art.web.warrior.tasking.model.CodingTask;
import org.art.web.warrior.tasking.model.CodingTaskDescriptor;
import org.art.web.warrior.tasking.repository.CodingTaskRepository;
import org.art.web.warrior.tasking.service.api.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
    public CodingTask updateTask(CodingTask modifiedTask) {
        String targetTaskNameId = modifiedTask.getDescriptor().getNameId();
        CodingTask targetTask = taskRepository.getCodingTaskByNameId(targetTaskNameId)
                .orElseThrow(() -> new TaskNotFoundException("Cannot find a coding task with such name ID!", targetTaskNameId));
        updateTargetTask(modifiedTask, targetTask);
        return taskRepository.save(targetTask);
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

    private void updateTargetTask(CodingTask modifiedTask, CodingTask targetTask) {
        CodingTaskDescriptor modifiedTaskDescriptor = modifiedTask.getDescriptor();
        targetTask.setDescriptor(modifiedTaskDescriptor);
        targetTask.setMethodSign(modifiedTask.getMethodSign());
        targetTask.setRunnerClassData(targetTask.getRunnerClassData());
    }
}
