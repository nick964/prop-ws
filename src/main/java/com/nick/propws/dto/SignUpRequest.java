package com.nick.propws.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String groupId;
}