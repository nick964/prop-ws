package com.nick.propws.controller;

import com.nick.propws.dto.CreateGroupReq;
import com.nick.propws.dto.CreateGroupResponse;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Question;
import com.nick.propws.entity.User;
import com.nick.propws.service.GroupService;
import com.nick.propws.service.UserDetailsImpl;
import com.nick.propws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("groups")
public class GroupController {
    @Autowired
    GroupService groupService;

    @PostMapping("/create")
    public @ResponseBody ResponseEntity<CreateGroupResponse> createGroup(@RequestBody CreateGroupReq createGroupReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loggedInUser = (UserDetailsImpl) authentication.getPrincipal();
        String userName = loggedInUser.getUsername();
        return ResponseEntity.ok(groupService.createGroup(createGroupReq, userName));
    }

    @PostMapping("/details")
    public @ResponseBody ResponseEntity<CreateGroupResponse> getDetails(@RequestParam Long groupId) {
        return null;
    }
}
