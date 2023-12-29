package com.nick.propws.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "question_option")
public class QuestionOptions {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String question_option;

    private String input_type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOption() {
        return question_option;
    }

    public void setOption(String option) {
        this.question_option = option;
    }

    public String getInput_type() {
        return input_type;
    }

    public void setInput_type(String input_type) {
        this.input_type = input_type;
    }

}
