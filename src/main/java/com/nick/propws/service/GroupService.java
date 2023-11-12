package com.nick.propws.service;

import com.nick.propws.dto.CreateGroupReq;
import com.nick.propws.dto.CreateGroupResponse;
import com.nick.propws.dto.GroupDetailsResponse;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupService {

    List<Group> getGroups();

    Group createGroup(CreateGroupReq createGroupReq);

    CreateGroupResponse createGroup(CreateGroupReq createGroupReq, String email);

    void addUserToGroup(User user, String groupId) throws Exception;

    ResponseEntity<?> getGroupDetail(Long groupId) throws Exception;


}
