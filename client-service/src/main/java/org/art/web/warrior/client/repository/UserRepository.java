package org.art.web.warrior.client.repository;

import org.art.web.warrior.client.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByEmail(String email);
}
