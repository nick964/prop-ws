package com.nick.propws.dto;

import lombok.Data;

import java.util.List;

@Data
public class MemberSubmissionDto {

    public int groupId;

    public List<MemberAnswerDto> answers;


}
