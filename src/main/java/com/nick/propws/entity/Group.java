package com.nick.propws.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity(name = "MYGROUPS")
@Data
public class Group {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private String groupKey;

    private int groupRole;

    private String icon;

    @OneToMany(mappedBy = "group")
    private List<Member> members;

}
