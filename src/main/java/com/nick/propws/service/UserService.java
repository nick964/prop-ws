package com.nick.propws.service;

import com.nick.propws.dto.ProfileResponse;
import com.nick.propws.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {

    User getUser(int userId);

    User getUserByLogin(String userName);

    Boolean checkIfUserExists(String email);

    List<User> getUsers();

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    ProfileResponse getProfile() throws UsernameNotFoundException;

}
