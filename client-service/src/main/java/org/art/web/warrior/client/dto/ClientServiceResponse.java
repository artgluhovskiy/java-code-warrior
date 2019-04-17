package org.art.web.warrior.client.dto;

import lombok.*;
import org.art.web.warrior.client.model.ClientResponseStatus;
import org.art.web.warrior.client.model.CompErrorDetails;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientServiceResponse {

    private String className;

    private String srcCode;

    private String message;

    private ClientResponseStatus respStatus;

    private CompErrorDetails compErrorDetails;
}
