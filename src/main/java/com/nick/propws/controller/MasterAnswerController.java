package com.nick.propws.controller;

import com.nick.propws.dto.AnswerDto;
import com.nick.propws.dto.MemberSubmissionDto;
import com.nick.propws.dto.TrackResponse;
import com.nick.propws.entity.User;
import com.nick.propws.service.MasterAnswerService;
import com.nick.propws.service.MemberService;
import com.nick.propws.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("master")
public class MasterAnswerController {

    @Autowired
    UserUtil userUtil;

    @Autowired
    MasterAnswerService masterAnswerService;

    @PostMapping("/add")
    public void submitAnswers(@RequestBody List<AnswerDto> answerDto) {
        User user = userUtil.getUserFromAuth();
        if(user == null) {
            System.out.println("do something here ");
        }
        masterAnswerService.addMasterAnswer(answerDto);
    }


}
