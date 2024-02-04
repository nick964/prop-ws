package com.nick.propws.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupResultsResponse {

    public boolean success;
    public boolean gameOver;
    public MemberDto winner;
    public GroupDto group;
    public List<MemberDto> members = new ArrayList<>();

}
