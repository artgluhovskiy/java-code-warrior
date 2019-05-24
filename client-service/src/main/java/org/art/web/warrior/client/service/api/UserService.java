package org.art.web.warrior.client.service.api;

import org.art.web.warrior.client.dto.UserDto;
import org.art.web.warrior.client.exception.EmailExistsException;
import org.art.web.warrior.client.model.User;

public interface UserService {

    User registerNewUserAccount(UserDto userDto) throws EmailExistsException;
}
