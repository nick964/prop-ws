package com.nick.propws.util;

import com.nick.propws.entity.User;
import com.nick.propws.service.UserDetailsImpl;
import com.nick.propws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

    @Autowired
    UserService userService;

    public User getUserFromAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loggedInUser = (UserDetailsImpl) authentication.getPrincipal();
        String userName = loggedInUser.getUsername();
        return userService.getUserByLogin(userName);
    }
}
