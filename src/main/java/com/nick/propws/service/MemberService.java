package com.nick.propws.service;

import com.nick.propws.dto.MemberSubmissionDto;
import com.nick.propws.dto.SubmissionResponse;
import com.nick.propws.dto.TrackResponse;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
import com.nick.propws.entity.User;
import com.nick.propws.exceptions.PropSheetException;

import java.util.List;

public interface MemberService {

    Member createMember(User user, Group g);


    void submitAnswers(MemberSubmissionDto memberSubmission, User user) throws PropSheetException;

    SubmissionResponse trackResponse(User user, Long groupId) throws PropSheetException;
}
