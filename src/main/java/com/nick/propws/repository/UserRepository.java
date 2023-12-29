package com.nick.propws.repository;

import com.nick.propws.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(int id);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query("SELECT u FROM users u WHERE u.username LIKE ?1 OR u.email LIKE ?1 OR u.name LIKE ?1")
    List<User> findUsersByKey(String key);




}
