package com.nick.propws.dto;

import com.nick.propws.entity.MemberAnswer;
import lombok.Data;

import java.util.List;

@Data
public class TrackResponse {

    public String questionText;

    public String section;

    public String answer;

    public String correctAnswer;

    public boolean isCorrect;

}
