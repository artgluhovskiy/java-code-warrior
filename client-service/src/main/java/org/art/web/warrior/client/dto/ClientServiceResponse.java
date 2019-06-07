package org.art.web.warrior.client.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientServiceResponse {

//    private String className;
//
//    private String srcCode;
//
//    private String solutionSrcCode;
//
//    private String runnerSrcCode;

    private String message;

    private String execMessage;

    private String respStatus;

    private CompErrorDetails compErrorDetails;
}
