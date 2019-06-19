package org.art.web.warrior.users.resource;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.users.dto.UserDto;
import org.art.web.warrior.users.exception.UserNotFoundException;
import org.art.web.warrior.users.model.User;
import org.art.web.warrior.users.repository.mysql.UserRepository;
import org.art.web.warrior.users.util.ServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserResource {

    private final UserRepository userRepository;

    @Autowired
    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserDto userDto) {
        User user = ServiceMapper.mapToUser(userDto);
        return userRepository.save(user);
    }

    @GetMapping("/{email}")
    public User findUserByEmail(@PathVariable("email") String email) {
        return userRepository.findUserByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Cannot find user with such email.", email));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
