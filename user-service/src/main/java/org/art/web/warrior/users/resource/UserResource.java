package org.art.web.warrior.users.resource;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.commons.users.dto.TaskOrderDto;
import org.art.web.warrior.commons.users.dto.UserDto;
import org.art.web.warrior.commons.users.validation.groups.OnCreate;
import org.art.web.warrior.commons.users.validation.groups.OnUpdate;
import org.art.web.warrior.users.exception.EmailExistsException;
import org.art.web.warrior.users.exception.RoleNotFoundException;
import org.art.web.warrior.users.exception.UserNotFoundException;
import org.art.web.warrior.users.model.Role;
import org.art.web.warrior.users.model.TaskOrder;
import org.art.web.warrior.users.model.User;
import org.art.web.warrior.users.repository.mysql.RoleRepository;
import org.art.web.warrior.users.repository.mysql.UserRepository;
import org.art.web.warrior.users.util.ServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.art.web.warrior.users.CommonServiceConstants.ROLE_USER;

@Slf4j
@Transactional
@RestController
@RequestMapping("/users")
public class UserResource {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserResource(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        String email = userDto.getEmail();
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user != null) {
            throw new EmailExistsException("User with such email already exists!", email);
        }
        user = ServiceMapper.mapToUser(userDto, passwordEncoder);
        Role role = roleRepository.findByName(ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("Cannot find role with such name.", ROLE_USER));
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    @GetMapping("/{email}")
    public User findUserByEmail(@PathVariable String email) {
        String decEmail = URLDecoder.decode(email, StandardCharsets.UTF_8.toString());
        return userRepository.findUserByEmail(decEmail)
                .orElseThrow(() -> new UserNotFoundException("Cannot find user with such email.", decEmail));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PutMapping
    public void updateUser(@Validated(OnUpdate.class) @RequestBody UserDto userDto) {
        User user;
        String email = userDto.getEmail();
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (!userOptional.isPresent()) {
            user = ServiceMapper.mapToUser(userDto, passwordEncoder);
            userRepository.save(user);
        } else {
            user = userOptional.get();
            ServiceMapper.updateUser(user, userDto);
            userRepository.save(user);
        }
    }

    @PutMapping("/{email}")
    public void addTaskOrder(@Valid @RequestBody TaskOrderDto taskOrderDto, @PathVariable String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Cannot find user with such email.", email));
        TaskOrder taskOrder = ServiceMapper.mapToTaskOrder(taskOrderDto);
        if (!user.getTaskOrders().contains(taskOrder)) {
            taskOrder.setRegDate(LocalDateTime.now());
            taskOrder.setUser(user);
            user.getTaskOrders().add(taskOrder);
            userRepository.save(user);
        }
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable String email) {
        userRepository.deleteUserByEmail(email);
    }
}
