package org.art.web.warrior.commons.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.art.web.warrior.commons.compiler.validation.PasswordMatches;
import org.art.web.warrior.commons.users.validation.groups.OnCreate;
import org.art.web.warrior.commons.users.validation.groups.OnUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.art.web.warrior.commons.CommonConstants.EMAIL_REGEXP;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches(groups = OnCreate.class)
public class UserDto {

    private boolean enabled;

    @NotBlank(message = "First name should not be blank!", groups = OnCreate.class)
    private String firstName;

    @NotBlank(message = "Last name should not be blank!", groups = OnCreate.class)
    private String lastName;

    @Email(regexp = EMAIL_REGEXP, message = "Invalid email address!", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @NotBlank(message = "Password should not be blank!", groups = {OnCreate.class, OnUpdate.class})
    private String password;

    @NotBlank(message = "Matching password should not be blank!", groups = OnCreate.class)
    private String matchingPassword;

    @Builder.Default
    private List<RoleDto> roles = Collections.emptyList();

    @Builder.Default
    private Set<TaskOrderDto> taskOrders = Collections.emptySet();

    private LocalDateTime regDate;
}
