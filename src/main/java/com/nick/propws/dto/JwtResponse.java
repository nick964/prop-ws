package com.nick.propws.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private Date jwtExpiration;
    private String type = "Bearer";
    private boolean success = true;
    private String errorMessage;
    private Long id;
    private String username;
    private List<String> roles;
    private String icon;
    private String name;
}
