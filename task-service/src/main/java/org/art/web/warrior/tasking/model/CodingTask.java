package org.art.web.warrior.tasking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @CreationTimestamp
    private LocalDateTime publicationDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
