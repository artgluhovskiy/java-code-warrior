package org.art.web.warrior.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.art.web.warrior.commons.compiler.validation.PasswordMatches;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Set;

import static org.art.web.warrior.client.CommonServiceConstants.EMAIL_REGEXP;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PasswordMatches
public class UserDto {

    @NotBlank(message = "First name should not be blank!")
    private String firstName;

    @NotBlank(message = "Last name should not be blank!")
    private String lastName;

    @Email(regexp = EMAIL_REGEXP, message = "Invalid email address!")
    private String email;

    @NotBlank(message = "Password should not be blank!")
    private String password;

    @NotBlank(message = "Matching password should not be blank!")
    private String matchingPassword;

    private Set<String> solvedTaskNameIds = Collections.emptySet();
}
