package com.nick.propws.controller;

import com.nick.propws.entity.Group;
import com.nick.propws.entity.Question;
import com.nick.propws.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("groups")
public class GroupController {
    @Autowired
    GroupService groupService;

    @PostMapping("/create")
    public @ResponseBody Group createGroup() {
        return groupService.createGroup();
    }
}
