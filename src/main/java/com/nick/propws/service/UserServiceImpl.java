package com.nick.propws.service;

import com.nick.propws.dto.GroupDto;
import com.nick.propws.dto.MemberDto;
import com.nick.propws.dto.ProfileResponse;
import com.nick.propws.dto.UserDto;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
import com.nick.propws.entity.User;
import com.nick.propws.repository.UserRepository;
import com.nick.propws.util.DtoMapper;
import com.nick.propws.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.nick.propws.util.DtoMapper.mapFromGroup;
import static com.nick.propws.util.DtoMapper.mapMember;

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found with username" + username);
        }
        return UserDetailsImpl.build(user);
    }

    @Override
    public ProfileResponse getProfile() throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loggedInUser = (UserDetailsImpl) authentication.getPrincipal();
        String userName = loggedInUser.getUsername();
        User user = userRepository.findUserByUsername(userName);
        if(user == null) {
            throw new UsernameNotFoundException("User not found with username" + userName);
        }
        ProfileResponse profileResponse = new ProfileResponse();
        for(Member m : user.getMembers()) {
            MemberDto memberDto = mapMember(m, true);
            profileResponse.getMembers().add(memberDto);
        }
        return profileResponse;
    }

    @Override
    public List<UserDto> searchUsers(String key) {
        if(!key.isEmpty()) {
            String searchParam = "%" + key.trim() + "%";
            List<User> users =  userRepository.findUsersByKey(searchParam);
            List<UserDto> userDtos = new ArrayList<>();
            for(User u : users) {
                UserDto userDto = DtoMapper.mapFromUser(u);
                userDtos.add(userDto);
            }
            return userDtos;
        } else {
            return new ArrayList<>();
        }
    }


}
