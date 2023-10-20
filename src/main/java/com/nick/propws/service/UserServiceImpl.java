package com.nick.propws.service;

import com.nick.propws.entity.User;
import com.nick.propws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;


    @Override
    public User getUser(int userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public User getUserByLogin(String userName) {
        return userRepository.findUserByUsername(userName);
    }

    @Override
    public Boolean checkIfUserExists(String email) {
        String myEmail = email.trim().toLowerCase();
        User us = userRepository.findUserByEmail(myEmail);
        return (us != null);
    }

    @Override
    public List<User> getUsers() {
        List<User> userList = userRepository.findAll();
        return userList;
    }
}