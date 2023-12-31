package com.nick.propws.service;

import com.nick.propws.dto.CreateGroupReq;
import com.nick.propws.dto.CreateGroupResponse;
import com.nick.propws.dto.GroupDetailsResponse;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
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

    @Value("${upload.dir}")
    private String uploadDir;


    @Override
    public List<Group> getGroups() {
        User currentUser = userUtil.getUserFromAuth();
        return null;

    }

    @Override
    public Group createGroup(CreateGroupReq req) {
        Group g = new Group();
//        g.setName(req.getName());
//        g.setIcon(req.getIcon());
//        g.setRole(1);
//        g.setKey(UUID.randomUUID().toString());
        return groupRepository.save(g);
    }

    @Override
    public CreateGroupResponse createGroup(CreateGroupReq createGroupReq, String username) {

        User user = userRepository.findUserByUsername(username);
        Group g = new Group();

        g.setName(createGroupReq.getName());
        g.setIcon(createGroupReq.getIcon());
        g.setGroupKey(UUID.randomUUID().toString());

        Group saved = groupRepository.save(g);

        Member m = new Member();
        m.setGroup(saved);
        m.setUser(user);
        m.setGroupAdmin(true);
        memberRepository.save(m);

        CreateGroupResponse res = new CreateGroupResponse();
        res.setId(saved.getId());
        res.setKey(g.getGroupKey());
        res.setName(g.getName());
        res.setDescription(createGroupReq.getDescription());


        return res;
    }

    @Override
    public void addUserToGroup(User user, String groupId){
        Long gId = Long.valueOf(groupId);
        Optional<Group> g = groupRepository.findById(gId);
        if(g.isEmpty()) {
            throw new PropSheetException("Group not found");
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
    public ResponseEntity<?> getGroupDetail(Long groupId) throws PropSheetException {

        Optional<Group> findGroup = this.groupRepository.findById(groupId);
        if(findGroup.isEmpty()) {
            throw new PropSheetException("No group found with id " + groupId);
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

    @Override
    public int getMemberPositionInGroup(Long memberId, Group g) throws PropSheetException {
        List<Member> members = g.getMembers();
        if(members.isEmpty()) {
            throw new PropSheetException("No members in group");
        }
        List<Long> scores = new ArrayList<>();
        for(Member m : members) {
            scores.add(m.getScore());
        }
        Collections.sort(scores);
        Collections.reverse(scores);
        int position = scores.indexOf(memberId);
        return position + 1;
    }


    private String saveImage(MultipartFile image) {
        try {
            // Generate a unique filename
            String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "_" + image.getOriginalFilename());

            // Resolve the upload directory
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

            // Ensure the directory exists, create if not
            Files.createDirectories(uploadPath);

            // Save the file to the server
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath);

            // Build and return the URL
            return "/uploads/" + fileName; // Adjust the URL structure as needed
        } catch (IOException ex) {
            throw new RuntimeException("Failed to save image", ex);
        }
    }
}
