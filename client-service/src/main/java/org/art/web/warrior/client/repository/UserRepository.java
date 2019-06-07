package org.art.web.warrior.client.repository;

import org.art.web.warrior.client.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);

    void deleteUserByEmail(String email);
}
