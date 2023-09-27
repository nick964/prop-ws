package com.nick.propws.service;

import com.nick.propws.entity.Question;
import com.nick.propws.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl  implements QuestionService{

    @Autowired
    QuestionRepository questionRepository;


    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
}
