package org.art.web.warrior.commons.tasking.message;

import lombok.*;
import org.art.web.warrior.commons.common.event.BaseMessage;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class TaskDeletionMessage extends BaseMessage {

    @Builder
    public TaskDeletionMessage(UUID id, String message, String nameId) {
        super(id, message);
        this.nameId = nameId;
        this.type = MessageType.DELETE;
    }

    private String nameId;

    private MessageType type;
}
