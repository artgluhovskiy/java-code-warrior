package org.art.web.warrior.tasking.service;

import org.art.web.warrior.tasking.exception.TaskNotFoundException;
import org.art.web.warrior.tasking.model.CodingTask;
import org.art.web.warrior.tasking.model.CodingTaskDescriptor;
import org.art.web.warrior.tasking.repository.mongo.CodingTaskMongoRepository;
import org.art.web.warrior.tasking.service.api.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final CodingTaskMongoRepository taskMongoRepository;

    @Autowired
    public TaskServiceImpl(CodingTaskMongoRepository taskMongoRepository) {
        this.taskMongoRepository = taskMongoRepository;
    }

    @Override
    public CodingTask publishTask(CodingTask task) {
        return taskMongoRepository.save(task);
    }

    @Override
    public CodingTask updateTask(CodingTask modifiedTask) {
        String targetTaskNameId = modifiedTask.getDescriptor().getNameId();
        CodingTask targetTask = taskMongoRepository.getCodingTaskByNameId(targetTaskNameId)
            .orElseThrow(() -> new TaskNotFoundException("Cannot find a coding task with such name ID!", targetTaskNameId));
        updateTargetTask(modifiedTask, targetTask);
        return taskMongoRepository.save(targetTask);
    }

    @Override
    public Optional<CodingTask> getTaskByNameId(String nameId) {
        return taskMongoRepository.getCodingTaskByNameId(nameId);
    }

    @Override
    public List<CodingTask> getAllTasks() {
        return taskMongoRepository.findAll();
    }

    @Override
    public void deleteTaskByNameId(String nameId) {
        taskMongoRepository.deleteByNameId(nameId);
    }

    private void updateTargetTask(CodingTask modifiedTask, CodingTask targetTask) {
        CodingTaskDescriptor modifiedTaskDescriptor = modifiedTask.getDescriptor();
        targetTask.setDescriptor(modifiedTaskDescriptor);
        targetTask.setMethodSign(modifiedTask.getMethodSign());
        targetTask.setRunnerClassData(targetTask.getRunnerClassData());
    }
}
