package com.nick.propws.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "invites")
@Data
public class Invite {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String invite_status;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @JsonIgnore
    private Group group;

    @ManyToOne
    @JoinColumn(name = "invitor_id", referencedColumnName = "id")
    @JsonIgnore
    private User invitor;

    @ManyToOne
    @JoinColumn(name = "invitee_id", referencedColumnName = "id")
    @JsonIgnore
    private User invitee;


}
