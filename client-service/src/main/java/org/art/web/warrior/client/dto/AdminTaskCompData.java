package org.art.web.warrior.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdminTaskCompData {

    @NotBlank(message = "Task Name ID should not be blank!")
    private String taskNameId;

    @NotBlank(message = "Task Name should not be blank!")
    private String taskName;

    @NotBlank(message = "Task Description should not be blank!")
    private String taskDescription;

    @NotBlank(message = "Task Method Signature should not be blank!")
    private String taskMethodSign;

    @NotBlank(message = "Solution Code should not be blank!")
    private String solutionSrcCode;

    @NotBlank(message = "Runner Code should not be blank!")
    private String runnerSrcCode;

    public boolean isValid() {
        return StringUtils.isNotBlank(solutionSrcCode)
                && StringUtils.isNotBlank(runnerSrcCode);
    }
}
