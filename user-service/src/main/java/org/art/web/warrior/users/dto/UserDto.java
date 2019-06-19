package org.art.web.warrior.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.art.web.warrior.commons.compiler.validation.PasswordMatches;
import org.art.web.warrior.users.CommonServiceConstants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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

    @Email(regexp = CommonServiceConstants.EMAIL_REGEXP, message = "Invalid email address!")
    private String email;

    @NotBlank(message = "Password should not be blank!")
    private String password;

    @NotBlank(message = "Matching password should not be blank!")
    private String matchingPassword;
}
