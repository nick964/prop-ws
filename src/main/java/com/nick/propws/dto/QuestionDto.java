package com.nick.propws.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionDto {
    public QuestionDto() {
        this.options = new ArrayList<>();
    }

    private Long id;

    private String text;

    private String questionType;

    private Double lineValue;

    private List<String> options;

    private String answer;

}
