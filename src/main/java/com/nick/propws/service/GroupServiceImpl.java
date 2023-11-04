package com.nick.propws.service;

import com.nick.propws.dto.CreateGroupReq;
import com.nick.propws.dto.CreateGroupResponse;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
import com.nick.propws.entity.User;
import com.nick.propws.repository.GroupRepository;
import com.nick.propws.repository.MemberRepository;
import com.nick.propws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GroupServiceImpl implements GroupService{

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemberRepository memberRepository;

    @Override
    public List<Group> getGroups() {
        return null;
    }

    @Override
    public Group createGroup(CreateGroupReq req) {
        Group g = new Group();
        g.setName(req.getName());
        g.setIcon(req.getIcon());
        g.setRole(1);
        g.setKey(UUID.randomUUID().toString());
        return groupRepository.save(g);
    }

    @Override
    public CreateGroupResponse createGroup(CreateGroupReq createGroupReq, String username) {

        User user = userRepository.findUserByUsername(username);
        Group g = new Group();

        g.setName(createGroupReq.getName());
        g.setIcon(createGroupReq.getIcon());
        g.setKey(UUID.randomUUID().toString());

        Group saved = groupRepository.save(g);

        Member m = new Member();
        m.setGroup(saved);
        m.setUser(user);
        memberRepository.save(m);

        CreateGroupResponse res = new CreateGroupResponse();
        res.setId(saved.getId());
        res.setKey(g.getKey());
        res.setName(g.getName());
        res.setDescription(createGroupReq.getDescription());


        return res;
    }
}
