
package com.web.puppylink.dao.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.puppylink.model.user.User;

public interface UserDao extends JpaRepository<User, String> {
    User getUserByEmail(String email);

    Optional<User> findUserByEmailAndPassword(String email, String password);
    Optional<User> findUserByEmailAndUid(String email, String uid);
    Optional<User> findUserByUid(String uid);
}
