package org.art.web.warrior.tasking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "codingTasks")
public class CodingTask {

    @Id
    private String id;

    private CodingTaskDescriptor descriptor;

    private String methodSign;

    private byte[] runnerClassData;

    private LocalDateTime publicationDate;

    private LocalDateTime updateDate;

    protected void onPublish() {
        this.publicationDate = LocalDateTime.now();
    }

    protected void noUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
