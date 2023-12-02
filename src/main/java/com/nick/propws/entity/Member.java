package com.nick.propws.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    private Long submission_status;

    private Long score;

    private boolean isGroupAdmin;

    @OneToMany(mappedBy = "member")
    private List<MemberAnswer> answers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getSubmission_status() {
        return submission_status;
    }

    public void setSubmission_status(Long submission_status) {
        this.submission_status = submission_status;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public List<MemberAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<MemberAnswer> answers) {
        this.answers = answers;
    }

    public boolean isGroupAdmin() {
        return isGroupAdmin;
    }

    public void setGroupAdmin(boolean groupAdmin) {
        isGroupAdmin = groupAdmin;
    }
}
