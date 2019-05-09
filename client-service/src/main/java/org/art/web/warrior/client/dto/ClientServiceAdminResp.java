package org.art.web.warrior.client.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientServiceAdminResp {

    private String solutionSrcCode;

    private String runnerSrcCode;

    private String message;

    private String respStatus;

    private CompErrorDetails compErrorDetails;
}
