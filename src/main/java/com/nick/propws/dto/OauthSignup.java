package com.nick.propws.dto;

import lombok.Data;

@Data
public class OauthSignup {
    private String username;
    private String email;
    private String img;
    private String provider;
    private String accessToken;
}