package org.art.web.warrior.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.art.web.warrior.client.config.validation.groups.OnPublishing;
import org.art.web.warrior.client.config.validation.groups.OnUpdate;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdminTaskPublicationData {

    @NotBlank(message = "Task Name ID should not be blank!", groups = {OnPublishing.class, OnUpdate.class})
    private String taskNameId;

    @NotBlank(message = "Task Name should not be blank!", groups = OnPublishing.class)
    private String taskName;

    @NotBlank(message = "Task Description should not be blank!", groups = OnPublishing.class)
    private String taskDescription;

    @NotBlank(message = "Task Method Signature should not be blank!", groups = OnPublishing.class)
    private String taskMethodSign;

    @NotBlank(message = "Solution Code should not be blank!", groups = OnPublishing.class)
    private String solutionSrcCode;

    @NotBlank(message = "Runner Code should not be blank!", groups = OnPublishing.class)
    private String runnerSrcCode;
}
