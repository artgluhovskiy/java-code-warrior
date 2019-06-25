package org.art.web.warrior.client.service.client.api;

import org.art.web.warrior.commons.users.dto.TaskOrderDto;
import org.art.web.warrior.commons.users.dto.UserDto;

public interface UserServiceClient {

    UserDto registerNewUserAccount(UserDto userDto);

    UserDto findUserByEmail(String email);

    void updateUser(UserDto userDto);

    void addTaskOrder(String email, TaskOrderDto taskOrderDto);

    void deleteUserByEmail(String email);
}
