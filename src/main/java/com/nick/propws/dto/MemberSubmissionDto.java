package com.nick.propws.dto;

import lombok.Data;

import java.util.List;

@Data
public class MemberSubmissionDto {

    public int groupId;

    public int finalSubmit = 0;

    public List<AnswerDto> answers;


}
