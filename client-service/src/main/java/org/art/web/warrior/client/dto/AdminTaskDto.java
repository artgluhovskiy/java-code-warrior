package org.art.web.warrior.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.art.web.warrior.client.config.validation.groups.OnDelete;
import org.art.web.warrior.client.config.validation.groups.OnPublishOrUpdate;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminTaskDto {

    @NotBlank(message = "Task Name ID should not be blank!", groups = {OnPublishOrUpdate.class, OnDelete.class})
    private String taskNameId;

    @NotBlank(message = "Task Name should not be blank!", groups = OnPublishOrUpdate.class)
    private String taskName;

    @NotBlank(message = "Task Description should not be blank!", groups = OnPublishOrUpdate.class)
    private String taskDescription;

    @NotBlank(message = "Task Method Signature should not be blank!", groups = OnPublishOrUpdate.class)
    private String taskMethodSign;

    @NotBlank(message = "Solution Code should not be blank!", groups = OnPublishOrUpdate.class)
    private String solutionSrcCode;

    @NotBlank(message = "Runner Code should not be blank!", groups = OnPublishOrUpdate.class)
    private String runnerSrcCode;
}
