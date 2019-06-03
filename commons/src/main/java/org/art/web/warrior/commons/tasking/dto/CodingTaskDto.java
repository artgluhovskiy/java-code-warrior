package org.art.web.warrior.commons.tasking.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingTaskDto {

    @NotBlank(message = "Task Name ID should not be blank!")
    private String nameId;

    @NotBlank(message = "Task Name should not be be blank!")
    private String name;

    @NotBlank(message = "Task description should not be blank!")
    private String description;

    @NotBlank(message = "Task method signature should not be blank!")
    private String methodSign;

    @NotEmpty(message = "Runner class data array should not be empty!")
    private byte[] runnerClassData;
}
