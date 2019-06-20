package org.art.web.warrior.users.config.data;

import org.art.web.warrior.users.CommonServiceConstants;
import org.art.web.warrior.users.model.Role;
import org.art.web.warrior.users.model.TaskOrder;
import org.art.web.warrior.users.model.User;
import org.art.web.warrior.users.repository.mysql.RoleRepository;
import org.art.web.warrior.users.repository.mysql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public InitialDataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        createRoleIfNotFound(CommonServiceConstants.ROLE_ADMIN);
        createRoleIfNotFound(CommonServiceConstants.ROLE_USER);
        createAdminIfNotFound();
        createUserIfNotFound();
        alreadySetup = true;
    }

    @Transactional
    protected void createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }

    @Transactional
    protected void createAdminIfNotFound() {
        String email = "admin@gmail.com";
        User admin = userRepository.findUserByEmail(email).orElse(null);
        if (admin == null) {
            User.UserBuilder builder = User.builder()
                .enabled(true)
                .firstName("Admin")
                .lastName("Admin")
                .password(encoder.encode("admin"))
                .email(email)
                .regDate(LocalDateTime.now());
            Role adminRole = roleRepository.findByName(CommonServiceConstants.ROLE_ADMIN);
            builder.roles(Collections.singletonList(adminRole));
            admin = builder.build();
            userRepository.save(admin);
        }
    }

    @Transactional
    protected void createUserIfNotFound() {
        String email = "user@gmail.com";
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user == null) {
            User.UserBuilder builder = User.builder()
                .enabled(true)
                .firstName("User")
                .lastName("User")
                .password(encoder.encode("user"))
                .email(email)
                .regDate(LocalDateTime.now());
            Role adminRole = roleRepository.findByName(CommonServiceConstants.ROLE_USER);
            builder.roles(Collections.singletonList(adminRole));
            user = builder.build();

            TaskOrder order1 = TaskOrder.builder()
                .nameId("task1")
                .name("Task 1")
                .description("Task 1 Description")
                .regDate(LocalDateTime.now())
                .user(user)
                .build();

            TaskOrder order2 = TaskOrder.builder()
                .nameId("task2")
                .name("Task 2")
                .description("Task 2 Description")
                .regDate(LocalDateTime.now())
                .user(user)
                .build();

            user.setTaskOrders(Arrays.asList(order1, order2));
            userRepository.save(user);
        }
    }
}
