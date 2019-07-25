package org.art.web.warrior.tasking.service;

import org.art.web.warrior.commons.common.event.BaseMessage;
import org.art.web.warrior.tasking.exception.TaskNotFoundException;
import org.art.web.warrior.tasking.model.CodingTask;
import org.art.web.warrior.tasking.model.CodingTaskDescriptor;
import org.art.web.warrior.tasking.repository.mongo.CodingTaskMongoRepository;
import org.art.web.warrior.tasking.service.api.TaskService;
import org.art.web.warrior.tasking.util.ServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.art.web.warrior.commons.CommonConstants.TASK_SERVICE_TOPIC_NAME;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final CodingTaskMongoRepository taskMongoRepository;

    private final KafkaTemplate<String, BaseMessage> kafkaTemplate;

    @Autowired
    public TaskServiceImpl(CodingTaskMongoRepository taskMongoRepository, KafkaTemplate<String, BaseMessage> kafkaTemplate) {
        this.taskMongoRepository = taskMongoRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public CodingTask publishTask(CodingTask task) {
        CodingTask publishedTask = taskMongoRepository.save(task);
//        kafkaTemplate.send(TASK_SERVICE_TOPIC_NAME, ServiceMapper.mapToTaskPubMessage(publishedTask));
        return publishedTask;
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
