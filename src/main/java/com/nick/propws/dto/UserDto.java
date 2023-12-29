package com.nick.propws.dto;

import lombok.Data;

@Data
public class UserDto {

    public Long id;

    public String name;

    private String email;

    private String username;

    private String icon;
    

}
