package com.nick.propws.service;

import com.nick.propws.dto.*;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
import com.nick.propws.entity.MemberAnswer;
import com.nick.propws.entity.User;
import com.nick.propws.exceptions.PropSheetException;
import com.nick.propws.repository.GroupRepository;
import com.nick.propws.repository.MemberRepository;
import com.nick.propws.repository.UserRepository;
import com.nick.propws.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.nick.propws.util.DtoMapper.mapFromGroup;
import static com.nick.propws.util.DtoMapper.mapMember;

@Service
public class GroupServiceImpl implements GroupService{

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StoreService storeService;

    @Autowired
    UserUtil userUtil;

    @Value("${upload.dir}")
    private String uploadDir;


    @Override
    public CreateGroupResponse createGroup(CreateGroupReq createGroupReq, String username) {

        User user = userRepository.findUserByUsername(username);
        Group g = new Group();

        g.setName(createGroupReq.getName());
        g.setDescription(createGroupReq.getDescription());
        g.setVenmoLink(createGroupReq.getVenmoLink());
        g.setGroupCost(createGroupReq.getGroupCost());

        g.setGroupKey(UUID.randomUUID().toString());

        if(createGroupReq.getIcon() != null) {
            UploadFileDto groupIcon = storeService.uploadFile(createGroupReq.getIcon());
            g.setIcon(groupIcon.getIconUrl());
            g.setIconObject(groupIcon.getObjectName());
        }
        Group saved = groupRepository.save(g);

        Member m = new Member();
        m.setGroup(saved);
        m.setUser(user);
        m.setGroupAdmin(true);
        m.setScore(0L);
        m.setSubmission_status(0L);
        memberRepository.save(m);

        CreateGroupResponse res = new CreateGroupResponse();
        res.setId(saved.getId());
        res.setKey(g.getGroupKey());
        res.setName(g.getName());
        res.setDescription(createGroupReq.getDescription());
        res.setVenmoLink(createGroupReq.getVenmoLink());

        return res;
    }

    @Override
    public void updateGroup(UpdateGroupReq updateGroupReq) {
        Optional<Group> group = groupRepository.findById(updateGroupReq.getId());
        if(group.isPresent()) {
            Group g = group.get();
            g.setName(StringUtils.hasText(updateGroupReq.getName()) ? updateGroupReq.getName() : g.getName());
            g.setDescription(StringUtils.hasText(updateGroupReq.getDescription()) ? updateGroupReq.getDescription() : g.getDescription());
            g.setVenmoLink(StringUtils.hasText(updateGroupReq.getVenmoLink()) ? updateGroupReq.getVenmoLink() : g.getVenmoLink());
            g.setGroupCost(updateGroupReq.getGroupCost() == null ? g.getGroupCost() : updateGroupReq.getGroupCost());
            if(updateGroupReq.getIcon() != null) {
                UploadFileDto groupIcon = storeService.uploadFile(updateGroupReq.getIcon());
                g.setIcon(groupIcon.getIconUrl());
            }
            groupRepository.save(g);
        } else {
            throw new PropSheetException("Group not found");
        }
    }

    @Override
    public void addUserToGroup(User user, String groupId){
        Long gId = Long.valueOf(groupId);
        Optional<Group> g = groupRepository.findById(gId);
        if(g.isEmpty()) {
            throw new PropSheetException("Group not found");
        }
        Member existingMember = memberRepository.findExistingMemberBeforeAdding(user.getId(), g.get().getId());
        if(existingMember != null) {
            System.out.println("User already in group - skipping add");
        } else {
            Group myGroup = g.get();
            Member newMember = new Member();
            newMember.setGroup(myGroup);
            newMember.setUser(user);
            newMember.setScore(0L);
            newMember.setSubmission_status(0L);
            newMember.setGroupAdmin(false);
            memberRepository.save(newMember);
            user.getMembers().add(newMember);
            userRepository.save(user);
        }
    }

    @Override
    public GroupDetailsResponse getGroupDetail(Long groupId) throws PropSheetException {

        Optional<Group> findGroup = this.groupRepository.findById(groupId);
        if(findGroup.isEmpty()) {
            throw new PropSheetException("No group found with id " + groupId);
        }
        return mapResponse(findGroup.get());
    }

    private GroupDetailsResponse mapResponse(Group g) {
        GroupDetailsResponse detailsResponse = new GroupDetailsResponse();
        detailsResponse.setName(g.getName());
        detailsResponse.setIcon(g.getIcon());
        detailsResponse.setMemberCount(g.getMembers().size());
        detailsResponse.setId(g.getId());
        Member m = findCurrentLeader(g.getMembers());
        MemberDto mDto = new MemberDto();
        mDto.setName(m.getUser().getName());
        mDto.setScore(getMemberScore(m));
        detailsResponse.setInLead(mDto);
        return detailsResponse;
    }

    private Long getMemberScore(Member m) {
        Long score = 0L;
        List<MemberAnswer> answers = m.getAnswers();
        if(answers != null && !answers.isEmpty()) {
            for(MemberAnswer answer : answers) {
                if(answer.getScore() != null) {
                    score += answer.getScore();
                }
            }
        }
        return score;
    }


    @Override
    public Member findCurrentLeader(List<Member> members) {
        if(members.isEmpty()) {
            return null;
        }
        List<Member> submitted = members.stream().filter(member -> member.getScore() != null
                && member.getSubmission_status() != null
                && member.getSubmission_status() == 1L).toList();
        return Collections.max(submitted, Comparator.comparing(Member::getScore));
    }

    @Override
    public DeleteGroupResponse deleteGroup(User user, Long groupId) {
        DeleteGroupResponse res = new DeleteGroupResponse();

        Group group = groupRepository.findGroupById(groupId);
        if(group == null) {
            res.setMessage("Group not found.");
            return res;
        }

        List<Member> members = group.getMembers();
        Long userId = user.getId();
        Member member = members.stream().filter(m -> m.getUser().getId().equals(userId)).findFirst().orElse(null);
        if(member == null) {
            res.setMessage("You are not a member of this group.");
            return res;
        }

        if(!member.isGroupAdmin()) {
            res.setMessage("You are not an admin of this group.");
            return res;
        }

        memberRepository.deleteAll(members);
        if(group.getIconObject() != null && !group.getIconObject().isEmpty()) {
            storeService.deleteFile(group.getIconObject());
        }

        res.setMessage("Group deleted successfully.");
        res.isDeleted = true;
        return res;
    }

    @Override
    public GroupResultsResponse getResultsForGroup(Long groupId) {
        GroupResultsResponse res = new GroupResultsResponse();
        Optional<Group> group = groupRepository.findById(groupId);
        if(group.isEmpty()) {
            res.setSuccess(false);
            res.setGameOver(false);
            return res;
        }
        Group g = group.get();
        List<Member> members = g.getMembers();
        if(members.isEmpty()) {
            res.setSuccess(false);
            res.setGameOver(false);
            return res;
        }
        List<Member> unsubmittedMembers = members.stream().filter(member ->
                member.getSubmission_status() == null || member.getSubmission_status() == 0L).toList();

        List<Member> submittedMembers = members.stream().filter(member ->
                member.getSubmission_status() != null && member.getSubmission_status() == 1L
                && member.getScore() != null).toList();

        List<Member> sortedMembers = submittedMembers.stream()
                .sorted(Comparator.comparing(Member::getScore).reversed())
                .toList();
        if(!sortedMembers.isEmpty()) {
            Member winner = sortedMembers.get(0);
            MemberDto winnerDto = mapMember(winner, false);
            res.setWinner(winnerDto);
        }

        List<MemberDto> memberDtos = new ArrayList<>();
        for(Member m : sortedMembers) {
            memberDtos.add(mapMember(m, false));
        }
        for(Member m : unsubmittedMembers) {
            memberDtos.add(mapMember(m, false));
        }
        res.setMembers(memberDtos);
        res.setSuccess(true);
        res.setGameOver(true);
        return res;
    }

    @Override
    public GroupResultsResponse getGlobalLeaderboard() {
        GroupResultsResponse res = new GroupResultsResponse();
        res.success = false;
        List<Member> members = memberRepository.findAll();
        if(members.isEmpty()) {
            return res;
        }
        List<Member> submittedMembers = members.stream().filter(member ->
                member.getSubmission_status() != null && member.getSubmission_status() == 1L
                && member.getScore() != null).toList();
        List<Member> sortedMembers = submittedMembers.stream()
                .sorted(Comparator.comparing(Member::getScore).reversed())
                .toList();
        List<MemberDto> memberDtos = new ArrayList<>();
        for(Member m : sortedMembers) {
            memberDtos.add(mapMember(m, true));
        }
        res.success = true;
        res.members = memberDtos;
        return res;
    }

    @Override
    public int getMemberPositionInGroup(Long memberId, Group g) throws PropSheetException {
        List<Member> members = g.getMembers();
        if(members.isEmpty()) {
            throw new PropSheetException("No members in group");
        }
        List<Member> submittedMembers = members.stream().filter(member ->
                member.getSubmission_status() != null && member.getSubmission_status() == 1L
                && member.getScore() != null).toList();
        List<Member> sortedMembers = submittedMembers.stream()
                .sorted(Comparator.comparing(Member::getScore).reversed())
                .toList();
        int position = IntStream.range(0, sortedMembers.size())
                .filter(i -> sortedMembers.get(i).getId().equals(memberId))
                .findFirst()
                .orElse(-1);
        return position + 1;
    }

    @Override
    public BasicGroupDetails getBasicDetails(Long groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if(group.isEmpty()) {
            return null;
        }
        Group g = group.get();
        BasicGroupDetails details = new BasicGroupDetails();
        details.setId(g.getId());
        details.setName(g.getName());
        details.setDescription(g.getDescription());
        details.setIcon(g.getIcon());
        details.setVenmoLink(g.getVenmoLink());
        details.setGroupCost(g.getGroupCost());
        if(g.getMembers() != null && !g.getMembers().isEmpty()) {
            Member admin = g.getMembers().stream().filter(Member::isGroupAdmin).findFirst().orElse(null);
            if(admin != null) {
                details.setAdmin(mapMember(admin, false));
            }
        }
        return details;
    }

}
