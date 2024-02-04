package com.nick.propws.service;

import com.nick.propws.dto.*;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
import com.nick.propws.entity.User;
import com.nick.propws.exceptions.PropSheetException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupService {

    CreateGroupResponse createGroup(CreateGroupReq createGroupReq, String email);

    void addUserToGroup(User user, String groupId) throws PropSheetException;

    GroupDetailsResponse getGroupDetail(Long groupId) throws PropSheetException;

    int getMemberPositionInGroup(Long memberId, Group g) throws PropSheetException;

    public Member findCurrentLeader(List<Member> members);

    DeleteGroupResponse deleteGroup(User user, Long groupId);

    GroupResultsResponse getResultsForGroup(Long groupId);

    GroupResultsResponse getGlobalLeaderboard();

    BasicGroupDetails getBasicDetails(Long groupId);


}
