package com.nick.propws.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String text;

    private String section;

    private int question_type;

    private Double line_value;
    @ManyToMany
    @JoinTable(
            name = "question_option_relation",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "question_option_id"))
    private List<QuestionOptions> questionOptions;

    @OneToOne(mappedBy = "question")
    private MasterAnswer masterAnswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(int question_type) {
        this.question_type = question_type;
    }

    public Double getLine_value() {
        return line_value;
    }

    public void setLine_value(Double line_value) {
        this.line_value = line_value;
    }

    public List<QuestionOptions> getOptions() {
        return questionOptions;
    }

    public void setOptions(List<QuestionOptions> questionOptions) {
        this.questionOptions = questionOptions;
    }

    public List<QuestionOptions> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(List<QuestionOptions> questionOptions) {
        this.questionOptions = questionOptions;
    }

    public MasterAnswer getMasterAnswer() {
        return masterAnswer;
    }

    public void setMasterAnswer(MasterAnswer masterAnswer) {
        this.masterAnswer = masterAnswer;
    }
}
