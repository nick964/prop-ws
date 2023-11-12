package com.nick.propws.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity(name = "mygroups")
@Data
public class Group {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String key;

    private int role;

    private String icon;

    @OneToMany(mappedBy = "group")
    private List<Member> members;

}
