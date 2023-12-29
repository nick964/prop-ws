package com.nick.propws.service;

import com.nick.propws.dto.ConfigDto;
import com.nick.propws.dto.QuestionDto;
import com.nick.propws.dto.QuestionSectionDto;
import com.nick.propws.entity.ConfigSetup;
import com.nick.propws.entity.Question;
import com.nick.propws.entity.QuestionOptions;
import com.nick.propws.repository.ConfigServiceRepository;
import com.nick.propws.repository.QuestionRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl  implements QuestionService{

    @Autowired
    QuestionRepository questionRepository;


    @Override
    public List<QuestionSectionDto> getAllQuestions() {

        List<Question> allQuestions = questionRepository.findAll();


        List<QuestionSectionDto> questionResponse = new ArrayList<>();
        List<QuestionDto> questionDtos = new ArrayList<>();

        String currentSection = "";
        Long count = 1L;
        QuestionSectionDto questionSectionDto = new QuestionSectionDto();
        for(Question q : allQuestions) {
            String sec = q.getSection();
            if(!currentSection.equals(sec)) {
                if(!StringUtils.isEmpty(questionSectionDto.getName())) {
                    questionResponse.add(questionSectionDto);
                }
                questionSectionDto = new QuestionSectionDto();
                count++;
                questionSectionDto.setName(sec);
                questionSectionDto.setId(count);
                questionSectionDto.getQuestions().add(mapFromQuestion(q));
            } else  {
                questionSectionDto.getQuestions().add(mapFromQuestion(q));
            }

            currentSection = sec;
        }
        questionResponse.add(questionSectionDto);


        return questionResponse;
    }

    private QuestionDto mapFromQuestion(Question q) {
        QuestionDto qDto = new QuestionDto();
        qDto.setId(q.getId());
        qDto.setText(q.getText());
        qDto.setQuestionType("radio");
        qDto.setLineValue(q.getLine_value());
        if(!q.getOptions().isEmpty()) {
            QuestionOptions opt = q.getOptions().get(0);
            String[] options = opt.getOption().split(",");
            for(String option : options) {
                qDto.getOptions().add(option);
            }
        }
        if(!(q.getMasterAnswer() == null)) {
            qDto.setAnswer(q.getMasterAnswer().getAnswer());
        }
        return qDto;
    }

    private ConfigDto mapFromConfigEntity(ConfigSetup c) {
        ConfigDto qDto = new ConfigDto();
        qDto.setRule(c.getRule());
        qDto.setEnabled(c.isEnabled());
        return qDto;
    }
}
