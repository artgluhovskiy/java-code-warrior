package org.art.web.warrior.users.config.data;

import org.art.web.warrior.users.CommonServiceConstants;
import org.art.web.warrior.users.model.Role;
import org.art.web.warrior.users.model.User;
import org.art.web.warrior.users.repository.mysql.RoleRepository;
import org.art.web.warrior.users.repository.mysql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public InitialDataLoader(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
    protected Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role = this.roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    protected User createAdminIfNotFound() {
        String email = "admin@gmail.com";
        User admin = userRepository.findUserByEmail(email).orElse(null);
        if (admin == null) {
            admin = new User();
            admin.setEnabled(true);
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setPassword("admin");
            admin.setEmail(email);
            Role adminRole = roleRepository.findByName(CommonServiceConstants.ROLE_ADMIN);
            admin.setRoles(Collections.singletonList(adminRole));
            userRepository.save(admin);
        }
        return admin;
    }

    @Transactional
    protected User createUserIfNotFound() {
        String email = "user@gmail.com";
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setEnabled(true);
            user.setFirstName("User");
            user.setLastName("User");
            user.setPassword("user");
            user.setEmail(email);
            Role adminRole = roleRepository.findByName(CommonServiceConstants.ROLE_USER);
            user.setRoles(Collections.singletonList(adminRole));
            userRepository.save(user);
        }
        return user;
    }
}
