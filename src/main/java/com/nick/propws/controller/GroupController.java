package com.nick.propws.controller;

import com.nick.propws.dto.CreateGroupReq;
import com.nick.propws.dto.CreateGroupResponse;
import com.nick.propws.dto.ShareGroupRequest;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Question;
import com.nick.propws.entity.User;
import com.nick.propws.service.GroupService;
import com.nick.propws.service.TwilioService;
import com.nick.propws.service.UserDetailsImpl;
import com.nick.propws.service.UserService;
import com.nick.propws.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    UserUtil userUtil;

    @Autowired
    TwilioService twilioService;

    @PostMapping("/create")
    public @ResponseBody ResponseEntity<CreateGroupResponse> createGroup(@RequestBody CreateGroupReq createGroupReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loggedInUser = (UserDetailsImpl) authentication.getPrincipal();
        String userName = loggedInUser.getUsername();
        return ResponseEntity.ok(groupService.createGroup(createGroupReq, userName));
    }

    @PostMapping("/addUser")
    public @ResponseBody ResponseEntity<Boolean> addUser(@RequestParam String groupId) {
        User user = userUtil.getUserFromAuth();
        groupService.addUserToGroup(user, groupId);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @PostMapping("/details")
    public @ResponseBody ResponseEntity<CreateGroupResponse> getDetails(@RequestParam Long groupId) {
        return null;
    }

    @PostMapping("/share")
    public @ResponseBody ResponseEntity<String> shareInvite(@RequestBody ShareGroupRequest shareReq) {
        if(shareReq.getInviteType().equals("sms")) {
            return twilioService.sendText(shareReq);
        } else if (shareReq.getInviteType().equals("email")) {
            return twilioService.sendEmail(shareReq);
        } else {
            return new ResponseEntity<>("Invite type is not supported", HttpStatus.BAD_REQUEST);
        }

    }
}
