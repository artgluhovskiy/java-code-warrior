package org.art.web.warrior.logger.repository;

import org.art.web.warrior.commons.tasking.message.TaskDeletionMessage;
import org.art.web.warrior.commons.tasking.message.TaskPublicationMessage;
import org.art.web.warrior.commons.tasking.message.TaskUpdateMessage;

public interface TaskServiceMessageRepository {

    void saveTaskPublicationMessage(TaskPublicationMessage message);
    void saveTaskUpdateMessage(TaskUpdateMessage message);
    void saveTaskDeletionMessage(TaskDeletionMessage message);
}
