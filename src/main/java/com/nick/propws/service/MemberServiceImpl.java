package com.nick.propws.service;

import com.nick.propws.dto.AnswerDto;
import com.nick.propws.dto.MemberSubmissionDto;
import com.nick.propws.entity.*;
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
    @Override
    public Member createMember(User user, Group group) {
        Member m = new Member();
        m.setUser(user);
        m.setGroup(group);
        return memberRepository.save(m);
    }

    @Override
    public void submitAnswers(MemberSubmissionDto memberSubmission, User user) {
        List<Member> members = user.getMembers()
                .stream()
                .filter(member -> member.getGroup().getId().intValue() == memberSubmission.getGroupId())
                .toList();
        if(members.isEmpty()) {
            System.out.println("throw error here, list shouldn't be empty");
        }
        Member member = members.get(0);
        List<MemberAnswer> existingSubmissions = memberAnswerRepository.findMemberAnswersByMember(member);
        List<MemberAnswer> memberAnswers = mapToMemberAnswers(memberSubmission, member);
        if(!existingSubmissions.isEmpty()) {
            memberAnswers = updateExistingSubmissions(existingSubmissions, memberAnswers);
        }
        memberAnswerRepository.saveAll(memberAnswers);
        member.setAnswers(memberAnswers);
        member.setSubmission_status(1L);
        memberRepository.save(member);
        System.out.println("Finish submission process");
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
