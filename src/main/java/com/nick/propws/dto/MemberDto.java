package com.nick.propws.dto;

import lombok.Data;

@Data
public class MemberDto {

    public String name;

    public Long questionId;

    public String answer;

    private Long submission_status;

    private Long score;

    private GroupDto groupDto;

    private String icon;

    private boolean isGroupAdmin;
    

}
