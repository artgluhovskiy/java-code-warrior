package org.art.web.warrior.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExecServiceRequest {

    private String solutionClassName;

    private byte[] solutionClassData;

    private String runnerClassName;

    private byte[] runnerClassData;

    @Override
    public String toString() {
        return "ExecServiceRequest{" +
                "solutionClassName: '" + solutionClassName + '\'' +
                ", solutionClassData (byte array length): " + solutionClassData.length +
                ", runnerClassName: '" + runnerClassName + '\'' +
                ", runnerClassData (byte array length): " + runnerClassData.length +
                '}';
    }
}
