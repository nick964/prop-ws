package com.nick.propws.entity;

import jakarta.persistence.*;

@Entity(name = "member_answers")
public class MemberAnswer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question ;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    private String answer;

    @Column(columnDefinition = "integer default 0")

    private Integer score;



}
