package com.nick.propws.controller;

import com.nick.propws.dto.*;
import com.nick.propws.entity.User;
import com.nick.propws.exceptions.PropSheetException;
import com.nick.propws.service.GroupService;
import com.nick.propws.service.NotificationService;
import com.nick.propws.service.UserDetailsImpl;
import com.nick.propws.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController()
@RequestMapping("groups")
public class GroupController {

    Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    GroupService groupService;

    @Autowired
    UserUtil userUtil;

    @Autowired
    NotificationService twilioService;

    @PostMapping("/create")
    public @ResponseBody ResponseEntity<CreateGroupResponse> createGroup(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestPart(value = "groupIcon", required = false) MultipartFile picture) {
        logger.info("Recieved request for group creation");
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loggedInUser = (UserDetailsImpl) authentication.getPrincipal();
            String userName = loggedInUser.getUsername();
            CreateGroupReq createGroupReq = new CreateGroupReq();
            createGroupReq.setName(name);
            createGroupReq.setDescription(description);
            createGroupReq.setIcon(picture);
            return ResponseEntity.ok(groupService.createGroup(createGroupReq, userName));
        } catch (Exception e) {
            logger.error("Error occurred during group upload");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new CreateGroupResponse());
        }

    }

    @PostMapping("/addUser")
    public @ResponseBody ResponseEntity<Boolean> addUser(@RequestParam String groupId) {
        User user = userUtil.getUserFromAuth();
        groupService.addUserToGroup(user, groupId);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping("/details")
    public @ResponseBody ResponseEntity<BasicGroupDetails> getDetails(@RequestParam Long groupId) {
        BasicGroupDetails res = new BasicGroupDetails();
        try {
            res = groupService.getBasicDetails(groupId);
            if(res == null) {
                logger.error("Error: No group details found for group id " + groupId);
                return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch(PropSheetException e) {
            logger.error("Error occurred fetching group details: " + e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
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

    @PostMapping("/delete")
    public @ResponseBody DeleteGroupResponse deleteGroup(@RequestBody DeleteGroupRequest deleteGroupRequest) {
        User user = userUtil.getUserFromAuth();
        return groupService.deleteGroup(user, deleteGroupRequest.getGroupId());
    }

    @GetMapping("/results")
    public @ResponseBody ResponseEntity<GroupResultsResponse> getGroupResults(@RequestParam("groupId") Long groupId) {
        return ResponseEntity.ok(groupService.getResultsForGroup(groupId));
    }

    @GetMapping("/leaderboard")
    public @ResponseBody ResponseEntity<GroupResultsResponse> getLeaderboard() {
        return ResponseEntity.ok(groupService.getGlobalLeaderboard());
    }
}
