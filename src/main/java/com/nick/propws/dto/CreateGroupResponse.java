package com.nick.propws.dto;

import lombok.Data;

@Data
public class CreateGroupResponse {

    public long id;

    public String key;

    public String name;

    public String icon;

    public String description;
    public String venmoLink;

    public Integer groupCost;

}
