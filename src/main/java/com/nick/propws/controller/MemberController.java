package com.nick.propws.controller;

import com.nick.propws.dto.MemberSubmissionDto;
import com.nick.propws.entity.Member;
import com.nick.propws.entity.User;
import com.nick.propws.service.MemberService;
import com.nick.propws.service.UserService;
import com.nick.propws.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("members")
public class MemberController {

    @Autowired
    UserUtil userUtil;

    @Autowired
    MemberService memberService;

    @PostMapping("/submit")
    public void submitAnswers(@RequestBody MemberSubmissionDto submission) {
        User user = userUtil.getUserFromAuth();
        if(user == null) {
            System.out.println("do something here ");
        }
        memberService.submitAnswers(submission, user);
    }


}
