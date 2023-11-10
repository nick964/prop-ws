package com.nick.propws.service;

import com.nick.propws.dto.MemberSubmissionDto;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
import com.nick.propws.entity.User;

public interface MemberService {

    Member createMember(User user, Group g);


    void submitAnswers(MemberSubmissionDto memberSubmission, User user);
}
