package com.nick.propws.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionSectionDto {

    public QuestionSectionDto() {
        this.questions = new ArrayList<>();
    }


    private Long id;
    private String name;
    private List<QuestionDto> questions;


}
