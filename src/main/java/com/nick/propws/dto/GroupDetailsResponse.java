package com.nick.propws.dto;

import com.nick.propws.entity.Member;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupDetailsResponse {

    public GroupDetailsResponse() {
        this.members = new ArrayList<>();
    }

    public long id;

    public String key;

    public String name;

    public String icon;

    public String description;

    public int memberCount;

    public List<Member> members;

    public Member inLead;

}
