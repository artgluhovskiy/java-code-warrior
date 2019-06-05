package org.art.web.warrior.commons.tasking.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class TaskDto {

    @Valid
    private TaskDescriptorDto descriptor;

    @NotBlank(message = "Task method signature should not be blank!")
    private String methodSign;

    @NotEmpty(message = "Runner class data array should not be empty!")
    private byte[] runnerClassData;

    private LocalDateTime publicationDate;

    private LocalDateTime updateDate;
}
