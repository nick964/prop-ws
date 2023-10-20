package com.nick.propws.service;

import com.nick.propws.dto.CreateGroupReq;
import com.nick.propws.entity.Group;
import com.nick.propws.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GroupServiceImpl implements GroupService{

    @Autowired
    GroupRepository groupRepository;

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
}
