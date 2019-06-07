package org.art.web.warrior.client.service;

import org.art.web.warrior.client.dto.UserDto;
import org.art.web.warrior.client.exception.EmailExistsException;
import org.art.web.warrior.client.model.Role;
import org.art.web.warrior.client.model.User;
import org.art.web.warrior.client.repository.RoleRepository;
import org.art.web.warrior.client.repository.UserRepository;
import org.art.web.warrior.client.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static org.art.web.warrior.client.CommonServiceConstants.ROLE_USER;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUserAccount(UserDto userDto) {
        if (emailExist(userDto.getEmail())) {
            throw new EmailExistsException("Account with such email already exists!", userDto.getEmail());
        }
        return registerNewAccount(userDto);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUserByEmail(String email) {
        userRepository.deleteUserByEmail(email);
    }

    private boolean emailExist(String email) {
        User user = userRepository.findUserByEmail(email);
        return user != null;
    }

    private User registerNewAccount(UserDto userDto) {
        User user = mapToUser(userDto);
        Role userRole = roleRepository.findByName(ROLE_USER);
        if (userRole == null) {
            userRole = new Role();
            userRole.setName(ROLE_USER);
        }
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }

    private User mapToUser(UserDto userDto) {
        return User.builder()
                .enabled(true)
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
    }
}
