package org.art.web.warrior.commons.tasking.message;

import lombok.*;
import org.art.web.warrior.commons.common.event.BaseMessage;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class TaskUpdateMessage extends BaseMessage {

    @Builder
    public TaskUpdateMessage(UUID id, String message, String nameId, String name, String description) {
        super(id, message);
        this.nameId = nameId;
        this.name = name;
        this.description = description;
        this.type = MessageType.UPDATE;
    }

    private String nameId;
    private String name;
    private String description;

    private MessageType type;
}
