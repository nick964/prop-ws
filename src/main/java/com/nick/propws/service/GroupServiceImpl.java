package com.nick.propws.service;

import com.nick.propws.dto.CreateGroupReq;
import com.nick.propws.dto.CreateGroupResponse;
import com.nick.propws.dto.GroupDetailsResponse;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
import com.nick.propws.entity.User;
import com.nick.propws.repository.GroupRepository;
import com.nick.propws.repository.MemberRepository;
import com.nick.propws.repository.UserRepository;
import com.nick.propws.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupServiceImpl implements GroupService{

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserUtil userUtil;


    @Override
    public List<Group> getGroups() {
        User currentUser = userUtil.getUserFromAuth();
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

    @Override
    public void addUserToGroup(User user, String groupId) throws Exception {
        Long gId = Long.valueOf(groupId);
        Optional<Group> g = groupRepository.findById(gId);
        if(g.isEmpty()) {
            throw new Exception("Group not found");
        }
        Group myGroup = g.get();
        Member newMember = new Member();
        newMember.setGroup(myGroup);
        newMember.setUser(user);
        newMember.setScore(0L);
        memberRepository.save(newMember);
        user.getMembers().add(newMember);
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<?> getGroupDetail(Long groupId) throws Exception {

        Optional<Group> findGroup = this.groupRepository.findById(groupId);
        if(findGroup.isEmpty()) {
            throw new Exception("No gropu found");
        }
        return ResponseEntity.ok(mapResponse(findGroup.get()));
    }

    private GroupDetailsResponse mapResponse(Group g) {
        GroupDetailsResponse detailsResponse = new GroupDetailsResponse();
        detailsResponse.setName(g.getName());
        detailsResponse.setIcon(g.getIcon());
        detailsResponse.setMemberCount(g.getMembers().size());
        detailsResponse.setInLead(findCurrentLeader(g.getMembers()));
        return detailsResponse;
    }

    private Member findCurrentLeader(List<Member> members) {
        if(members.isEmpty()) {
            return null;
        }
        return Collections.max(members, Comparator.comparing(Member::getScore));
    }
}
