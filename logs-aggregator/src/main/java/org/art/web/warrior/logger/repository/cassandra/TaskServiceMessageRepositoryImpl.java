package org.art.web.warrior.logger.repository.cassandra;

import org.art.web.warrior.commons.tasking.message.TaskDeletionMessage;
import org.art.web.warrior.commons.tasking.message.TaskPublicationMessage;
import org.art.web.warrior.commons.tasking.message.TaskUpdateMessage;
import org.art.web.warrior.logger.config.cassandra.CassandraConnector;
import org.art.web.warrior.logger.repository.TaskServiceMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.art.web.warrior.logger.ServiceCommonConstants.TASK_MESSAGE_COLUMN_FAMILY_NAME;

@Repository
public class TaskServiceMessageRepositoryImpl implements TaskServiceMessageRepository {

    private final CassandraConnector connector;

    @Autowired
    public TaskServiceMessageRepositoryImpl(CassandraConnector connector) {
        this.connector = connector;
    }

    @Override
    public void saveTaskPublicationMessage(TaskPublicationMessage message) {

    }

    @Override
    public void saveTaskUpdateMessage(TaskUpdateMessage message) {

    }

    @Override
    public void saveTaskDeletionMessage(TaskDeletionMessage message) {

    }

    public void createTable() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
            .append(TASK_MESSAGE_COLUMN_FAMILY_NAME).append("(")
            .append("id uuid PRIMARY KEY, ")
            .append("task_name text, ")
            .append("message text,")
            .append("type text,")
            .append("created timestamp,")
            .append("payload text);");
        String query = sb.toString();
        connector.getSession().execute(query);
    }
}
