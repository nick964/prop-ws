package com.nick.propws.service;

import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
import com.nick.propws.entity.User;
import com.nick.propws.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class MemberServiceImpl implements  MemberService{

    @Autowired
    MemberRepository memberRepository;
    @Override
    public Member createMember(User user, Group group) {
        Member m = new Member();
        m.setUser(user);
        m.setGroup(group);
        return memberRepository.save(m);
    }
}
