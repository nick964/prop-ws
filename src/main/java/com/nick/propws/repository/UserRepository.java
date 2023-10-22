package com.nick.propws.repository;

import com.nick.propws.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(int id);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);



}
