package com.nick.propws.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProfileResponse {

    public ProfileResponse() {
        this.members = new ArrayList<>();
    }

    public List<MemberDto> members;

}
