package org.art.web.warrior.tasking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private CodingTaskDescriptor descriptor;

    private String methodSign;

    @Lob
    private byte[] runnerClassData;

    private LocalDateTime publicationDate;

    private LocalDateTime updateDate;

    @PrePersist
    protected void onPublish() {
        this.publicationDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void noUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
