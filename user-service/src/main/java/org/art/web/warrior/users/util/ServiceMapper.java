package org.art.web.warrior.users.util;

import org.art.web.warrior.commons.users.dto.TaskOrderDto;
import org.art.web.warrior.commons.users.dto.UserDto;
import org.art.web.warrior.users.model.TaskOrder;
import org.art.web.warrior.users.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ServiceMapper {

    private ServiceMapper() {
    }

    public static User mapToUser(UserDto userDto, PasswordEncoder passwordEncoder) {
        Objects.requireNonNull(userDto, "User DTO should not be null!");
        return User.builder()
            .enabled(true)
            .firstName(userDto.getFirstName())
            .lastName(userDto.getLastName())
            .email(userDto.getEmail())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .build();
    }

    public static void updateUser(User user, UserDto userDto) {
        Objects.requireNonNull(userDto, "User DTO should not be null!");
        String firstName = userDto.getFirstName();
        if (isNotBlank(firstName)) {
            user.setFirstName(firstName);
        }
        String lastName = userDto.getLastName();
        if (isNotBlank(lastName)) {
            user.setLastName(lastName);
        }
    }

    public static TaskOrder mapToTaskOrder(TaskOrderDto taskOrderDto) {
        Objects.requireNonNull(taskOrderDto, "Task Order DTO should not be null!");
        return TaskOrder.builder()
            .nameId(taskOrderDto.getNameId())
            .name(taskOrderDto.getName())
            .description(taskOrderDto.getDescription())
            .build();
    }
}
