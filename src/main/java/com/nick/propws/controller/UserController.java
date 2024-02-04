package com.nick.propws.controller;

import com.nick.propws.dto.CreateGroupReq;
import com.nick.propws.dto.ProfileResponse;
import com.nick.propws.dto.UserDto;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.User;
import com.nick.propws.service.GroupService;
import com.nick.propws.service.StoreService;
import com.nick.propws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController()
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    StoreService storageService;

    @GetMapping("/find")
    public @ResponseBody User getUser(@RequestParam String userName) {
        return userService.getUserByLogin(userName);
    }

    @GetMapping("/findAll")
    public @ResponseBody List<User> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/exists")
    public @ResponseBody Boolean checkIfExists(@RequestParam String email) {
        return userService.checkIfUserExists(email);
    }

    @GetMapping("/profile")
    public @ResponseBody ProfileResponse getProfile() {
        return userService.getProfile();
    }

    @GetMapping("/search")
    public @ResponseBody List<UserDto> searchUsers(@RequestParam String searchKey) {
        return userService.searchUsers(searchKey);
    }

}
