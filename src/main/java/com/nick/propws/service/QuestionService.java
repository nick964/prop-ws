package com.nick.propws.service;

import com.nick.propws.dto.QuestionSectionDto;
import com.nick.propws.entity.Question;

import java.util.List;

public interface QuestionService {

    List<QuestionSectionDto> getAllQuestions();
}
