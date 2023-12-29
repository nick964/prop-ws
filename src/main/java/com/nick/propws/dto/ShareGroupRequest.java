package com.nick.propws.dto;

import lombok.Data;

@Data
public class ShareGroupRequest {

    private String groupId;

    private String name;
    private String inviteType;

    private String recipient;
}