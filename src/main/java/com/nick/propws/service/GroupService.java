package com.nick.propws.service;

import com.nick.propws.entity.Group;

import java.util.List;

public interface GroupService {

    List<Group> getGroups();

    Group createGroup();
}
