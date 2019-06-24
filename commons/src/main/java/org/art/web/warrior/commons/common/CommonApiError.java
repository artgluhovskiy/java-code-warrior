package org.art.web.warrior.commons.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonApiError {

    private int respStatusCode;

    private String respStatus;

    private String message;

    private LocalDateTime dateTime;
}
