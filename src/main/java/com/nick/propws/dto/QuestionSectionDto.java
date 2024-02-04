package com.nick.propws.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionSectionDto {

    public QuestionSectionDto() {
        this.questions = new ArrayList<>();
        this.configRules = new ArrayList<>();
    }


    private Long id;
    private int sortOrder;
    private String name;
    private List<QuestionDto> questions;
    private List<ConfigDto> configRules;



}
