package com.nick.propws.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "mygroups")
public class Group {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String group_key;

    private int role;

    private String icon;

    @OneToMany(mappedBy = "group")
    private List<Member> members;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return group_key;
    }

    public void setKey(String key) {
        this.group_key = key;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
