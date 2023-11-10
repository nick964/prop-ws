package com.nick.propws.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "member_answers")
@Data
public class MemberAnswer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @JsonIgnore
    private Question question ;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    @JsonIgnore
    private Member member;

    private String answer;

    @Column(columnDefinition = "integer default 0")

    private Integer score;



}
