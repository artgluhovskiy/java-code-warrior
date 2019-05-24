package org.art.web.warrior.client.service;

import org.art.web.warrior.client.dto.UserDto;
import org.art.web.warrior.client.exception.EmailExistsException;
import org.art.web.warrior.client.model.User;
import org.art.web.warrior.client.repository.UserRepository;
import org.art.web.warrior.client.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static org.art.web.warrior.client.CommonServiceConstants.ROLE_USER;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerNewUserAccount(UserDto userDto) throws EmailExistsException {
        if (emailExist(userDto.getEmail())) {
            throw new EmailExistsException("Account with such email already exists!", userDto.getEmail());
        }
        return userRepository.save(mapToUser(userDto));
    }

    private boolean emailExist(String email) {
        User user = userRepository.findUserByEmail(email);
        return user != null;
    }

    private User mapToUser(UserDto userDto) {
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(Collections.singletonList(ROLE_USER))
                .build();
    }
}
