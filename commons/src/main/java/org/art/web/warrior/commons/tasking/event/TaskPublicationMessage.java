package org.art.web.warrior.commons.tasking.event;

import lombok.*;
import org.art.web.warrior.commons.common.event.BaseMessage;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class TaskPublicationMessage extends BaseMessage {

    @Builder
    public TaskPublicationMessage(String message, LocalDateTime pubDate, String nameId, String name, String description) {
        super(message, pubDate);
        this.nameId = nameId;
        this.name = name;
        this.description = description;
    }

    String nameId;
    String name;
    String description;
}
