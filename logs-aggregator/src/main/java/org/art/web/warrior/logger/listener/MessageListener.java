package org.art.web.warrior.logger.listener;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.commons.common.event.BaseMessage;
import org.art.web.warrior.commons.tasking.message.TaskDeletionMessage;
import org.art.web.warrior.commons.tasking.message.TaskPublicationMessage;
import org.art.web.warrior.commons.tasking.message.TaskUpdateMessage;
import org.art.web.warrior.logger.repository.TaskServiceMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static org.art.web.warrior.commons.CommonConstants.TASK_SERVICE_TOPIC_NAME;

@Slf4j
@Component
public class MessageListener {

    private final TaskServiceMessageRepository messageRepository;

    @Autowired
    public MessageListener(TaskServiceMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = TASK_SERVICE_TOPIC_NAME, containerFactory = "taskServiceListenerFactory")
    public void listenTaskServiceMessage(BaseMessage message) {
        log.info("Message received: message type - {}, payload - {}", message.getClass().getSimpleName(), message);
        if (message instanceof TaskPublicationMessage) {
            TaskPublicationMessage pubMessage = (TaskPublicationMessage) message;
            messageRepository.saveTaskPublicationMessage(pubMessage);
        } else if (message instanceof TaskUpdateMessage) {
            TaskUpdateMessage updateMessage = (TaskUpdateMessage) message;
            messageRepository.saveTaskUpdateMessage(updateMessage);
        } else if (message instanceof TaskDeletionMessage) {
            TaskDeletionMessage deletionMessage = (TaskDeletionMessage) message;
            messageRepository.saveTaskDeletionMessage(deletionMessage);
        } else {
            throw new RuntimeException("Unknown message type received! Message: " + message);
        }
    }
}
