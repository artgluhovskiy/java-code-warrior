package org.art.web.warrior.commons.tasking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDescriptorDto {

    @NotBlank(message = "Task Name ID should not be blank!")
    private String nameId;

    @NotBlank(message = "Task Name should not be be blank!")
    private String name;

    @NotBlank(message = "Task description should not be blank!")
    private String description;

    private int rating;
}
