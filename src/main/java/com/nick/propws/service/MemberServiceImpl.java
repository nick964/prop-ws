package com.nick.propws.service;

import com.nick.propws.dto.AnswerDto;
import com.nick.propws.dto.MemberSubmissionDto;
import com.nick.propws.dto.SubmissionResponse;
import com.nick.propws.dto.TrackResponse;
import com.nick.propws.entity.*;
import com.nick.propws.exceptions.PropSheetException;
import com.nick.propws.repository.MemberAnswerRepository;
import com.nick.propws.repository.MemberRepository;
import com.nick.propws.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class MemberServiceImpl implements  MemberService{

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    MemberAnswerRepository memberAnswerRepository;

    @Autowired
    GroupService groupService;

    @Override
    public Member createMember(User user, Group group) {
        Member m = new Member();
        m.setUser(user);
        m.setGroup(group);
        return memberRepository.save(m);
    }

    @Override
    public void submitAnswers(MemberSubmissionDto memberSubmission, User user) throws PropSheetException {
        List<Member> members = user.getMembers()
                .stream()
                .filter(member -> member.getGroup().getId().intValue() == memberSubmission.getGroupId())
                .toList();
        if(members.isEmpty()) {
            throw new PropSheetException("No members associated with user");
        }
        Member member = members.get(0);
        if(member.getSubmission_status() != null && member.getSubmission_status() == 1) {
            throw new PropSheetException("This member has already submitted an entry for this group");
        }
        List<MemberAnswer> existingSubmissions = memberAnswerRepository.findMemberAnswersByMember(member);
        List<MemberAnswer> memberAnswers = mapToMemberAnswers(memberSubmission, member);
        if(!existingSubmissions.isEmpty()) {
            memberAnswers = updateExistingSubmissions(existingSubmissions, memberAnswers);
        }
        memberAnswerRepository.saveAll(memberAnswers);
        member.setAnswers(memberAnswers);
        member.setSubmission_status((long) memberSubmission.getFinalSubmit());
        memberRepository.save(member);
        System.out.println("Finish submission process");
    }

    @Override
    public SubmissionResponse trackResponse(User user, Long groupId) throws PropSheetException {
        List<Member> members = user.getMembers()
                .stream()
                .filter(member -> member.getGroup().getId().intValue() == groupId)
                .toList();
        if(members.isEmpty()) {
            throw new PropSheetException("No members associated with user");
        }
        List<TrackResponse> res = new ArrayList<>();
        Member member = members.get(0);
        List<MemberAnswer> existingSubmissions = memberAnswerRepository.findMemberAnswersByMember(member);
        for(MemberAnswer ans : existingSubmissions) {
            TrackResponse trackResponse = getTrackResponse(ans);
            res.add(trackResponse);
        }
        int position = groupService.getMemberPositionInGroup(member.getId(), member.getGroup());
        int score = 0;
        for(TrackResponse trackResponse : res) {
            if(trackResponse.isCorrect()) {
                score++;
            }
        }
        SubmissionResponse response = new SubmissionResponse();
        response.setResponses(res);
        response.setPosition(position);
        response.setTotalScore(score);
        return  response;
    }

    private static TrackResponse getTrackResponse(MemberAnswer ans) {
        TrackResponse trackResponse = new TrackResponse();
        trackResponse.setQuestionText(ans.getQuestion().getText());
        trackResponse.setSection(ans.getQuestion().getSection());
        trackResponse.setCorrect(ans.getScore() != null && ans.getScore() == 1);
        trackResponse.setAnswer(ans.getAnswer());
        if(ans.getQuestion().getMasterAnswer() != null) {
            trackResponse.setCorrectAnswer(ans.getQuestion().getMasterAnswer().getAnswer());
        } else {
            trackResponse.setCorrectAnswer("");
        }
        return trackResponse;
    }


    private List<MemberAnswer> mapToMemberAnswers(MemberSubmissionDto memberSubmission, Member member) {
        List<MemberAnswer> memberAnswers = new ArrayList<>();
        for(AnswerDto answer : memberSubmission.getAnswers()) {
            Question q = questionRepository.getReferenceById(answer.getQuestionId());
            MemberAnswer ans = new MemberAnswer();
            ans.setAnswer(answer.getAnswer());
            ans.setMember(member);
            ans.setQuestion(q);
            memberAnswers.add(ans);
        }
        return memberAnswers;
    }

    private List<MemberAnswer> updateExistingSubmissions(List<MemberAnswer> existingSubmissions, List<MemberAnswer> memberAnswers) {
        for(MemberAnswer existingSubmission : existingSubmissions) {
            for(MemberAnswer memberAnswer : memberAnswers) {
                if(existingSubmission.getQuestion().getId().intValue() == memberAnswer.getQuestion().getId().intValue()) {
                    existingSubmission.setAnswer(memberAnswer.getAnswer());
                }
            }
        }
        return existingSubmissions;
    }


}
