package org.art.web.warrior.logger.listener;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.commons.common.event.BaseMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static org.art.web.warrior.commons.CommonConstants.TASK_SERVICE_TOPIC_NAME;

@Slf4j
@Component
public class MessageListener {

    @KafkaListener(topics = TASK_SERVICE_TOPIC_NAME, containerFactory = "taskServiceListenerFactory")
    public void listenTaskServiceMessage(BaseMessage message) {
        log.info("Message received: payload - {}", message);
    }
}
