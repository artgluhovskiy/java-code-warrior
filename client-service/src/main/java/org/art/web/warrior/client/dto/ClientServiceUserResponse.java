package org.art.web.warrior.client.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientServiceUserResponse {

    private String className;

    private String srcCode;

    private String message;

    private String respStatus;

    private CompErrorDetails compErrorDetails;
}
