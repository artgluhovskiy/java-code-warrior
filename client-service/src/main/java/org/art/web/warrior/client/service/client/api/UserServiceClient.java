package org.art.web.warrior.client.service.client.api;

import org.art.web.warrior.commons.users.dto.TaskOrderDto;
import org.art.web.warrior.commons.users.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserServiceClient {

    ResponseEntity<UserDto> registerNewUserAccount(UserDto userDto);

    ResponseEntity<UserDto> findUserByEmail(String email);

    void updateUser(UserDto userDto);

    void addTaskOrder(String email, TaskOrderDto taskOrderDto);

    void deleteUserByEmail(String email);
}
