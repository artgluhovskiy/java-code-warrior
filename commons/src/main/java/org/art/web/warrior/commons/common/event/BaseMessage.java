package org.art.web.warrior.commons.common.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.art.web.warrior.commons.tasking.message.TaskDeletionMessage;
import org.art.web.warrior.commons.tasking.message.TaskPublicationMessage;
import org.art.web.warrior.commons.tasking.message.TaskUpdateMessage;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = TaskPublicationMessage.class, name = "TaskPublicationMessage.class"),
    @JsonSubTypes.Type(value = TaskUpdateMessage.class, name = "TaskUpdateMessage"),
    @JsonSubTypes.Type(value = TaskDeletionMessage.class, name = "TaskDeletionMessage")
})
public class BaseMessage {

    private UUID id;

    private String message;

    public BaseMessage(UUID id, String message) {
        this.id = id;
        this.message = message;
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime created = LocalDateTime.now();
}
