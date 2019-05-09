package org.art.web.warrior.client.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientServiceUserResp {

    private String className;

    private String srcCode;

    private String message;

    private String execMessage;

    private String respStatus;

    private CompErrorDetails compErrorDetails;
}
