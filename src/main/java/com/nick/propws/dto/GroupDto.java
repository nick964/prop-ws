package com.nick.propws.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class GroupDto {

    private Long id;

    private String name;

    private String groupKey;

    private int groupRole;

    private String icon;

    private int memberCount;

    private String venmoLink;

    private Integer cost;
    

}
