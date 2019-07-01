package org.art.web.warrior.tasking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coding_tasks")
public class CodingTask {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private CodingTaskDescriptor descriptor;

    @NotNull
    @Column(name = "methodSign")
    private String methodSign;

    @NotNull
    @Lob
    @Column(name = "binary_data")
    private byte[] runnerClassData;

    @Column(name = "publication_date")
    private LocalDateTime publicationDate;

    @Column(name = "modification_date")
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
