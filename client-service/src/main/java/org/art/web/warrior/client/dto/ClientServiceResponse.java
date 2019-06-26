package org.art.web.warrior.client.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientServiceResponse {

    private String message;

    private String execMessage;

    private String respStatus;

    private CompErrorDetails compErrorDetails;
}
