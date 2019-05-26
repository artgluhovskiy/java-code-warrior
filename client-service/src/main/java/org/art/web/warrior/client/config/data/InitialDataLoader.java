package org.art.web.warrior.client.config.data;

import org.art.web.warrior.client.model.Role;
import org.art.web.warrior.client.model.User;
import org.art.web.warrior.client.repository.RoleRepository;
import org.art.web.warrior.client.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.art.web.warrior.client.CommonServiceConstants.ROLE_ADMIN;
import static org.art.web.warrior.client.CommonServiceConstants.ROLE_USER;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitialDataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) return;

        createRoleIfNotFound(ROLE_ADMIN);
        createRoleIfNotFound(ROLE_USER);

        Role adminRole = roleRepository.findByName(ROLE_USER);
        User user = new User();
        user.setEnabled(true);
        user.setFirstName("User");
        user.setLastName("User");
        user.setPassword(passwordEncoder.encode("user"));
        user.setEmail("user@user.com");
        user.setRoles(Collections.singletonList(adminRole));
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    protected Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role = this.roleRepository.save(role);
        }
        return role;
    }
}
