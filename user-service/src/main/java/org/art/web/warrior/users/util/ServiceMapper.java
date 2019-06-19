package org.art.web.warrior.users.util;

import org.art.web.warrior.users.dto.UserDto;
import org.art.web.warrior.users.model.User;

import java.util.Objects;

public class ServiceMapper {

    private ServiceMapper() {}

    public static User mapToUser(UserDto userDto) {
        Objects.requireNonNull(userDto, "User DTO should not be null!");
        return User.builder()
            .enabled(true)
            .firstName(userDto.getFirstName())
            .lastName(userDto.getLastName())
            .email(userDto.getEmail())
            .password(userDto.getPassword())
            .build();
    }
}
