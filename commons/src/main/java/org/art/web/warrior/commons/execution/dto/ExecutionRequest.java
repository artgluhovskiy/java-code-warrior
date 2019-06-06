package org.art.web.warrior.commons.execution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionRequest {

    @NotBlank(message = "Solution class name should not be blank!")
    private String solutionClassName;

    @NotEmpty(message = "Solution class byte array should not be empty!")
    private byte[] solutionClassData;

    @NotBlank(message = "Runner class name should not be blank!")
    private String runnerClassName;

    @NotEmpty(message = "Runner class byte array should not empty!")
    private byte[] runnerClassData;

    @Override
    public String toString() {
        return "ExecutionRequest{" +
                "solutionClassName: '" + solutionClassName + '\'' +
                ", solutionClassData (byte array length): " + solutionClassData.length +
                ", runnerClassName: '" + runnerClassName + '\'' +
                ", runnerClassData (byte array length): " + runnerClassData.length +
                '}';
    }
}
