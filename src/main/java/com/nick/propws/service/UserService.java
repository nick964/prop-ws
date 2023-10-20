package com.nick.propws.service;

import com.nick.propws.entity.User;

import java.util.List;

public interface UserService {

    User getUser(int userId);

    User getUserByLogin(String userName);

    Boolean checkIfUserExists(String email);

    List<User> getUsers();

}
