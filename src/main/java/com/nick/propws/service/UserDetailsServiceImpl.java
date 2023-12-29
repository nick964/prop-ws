package com.nick.propws.service;

import com.nick.propws.entity.User;
import com.nick.propws.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    public UserDetailsServiceImpl(){
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return UserDetailsImpl.build(user);
    }
}