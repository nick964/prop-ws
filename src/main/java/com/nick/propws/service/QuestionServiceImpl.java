package com.nick.propws.service;

import com.nick.propws.dto.QuestionDto;
import com.nick.propws.dto.QuestionSectionDto;
import com.nick.propws.entity.Question;
import com.nick.propws.entity.QuestionOptions;
import com.nick.propws.repository.QuestionRepository;
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
        for(Question q : allQuestions) {
            String sec = q.getSection();
            if(!currentSection.equals(sec)) {
                QuestionSectionDto questionSectionDto = new QuestionSectionDto();
                count++;
                questionSectionDto.setName(sec);
                questionSectionDto.setId(count);
                questionSectionDto.getQuestions().add(mapFromQuestion(q));
            }

            currentSection = sec;
            //questionDtos.add(mapFromQuestion(q));
        }


        QuestionSectionDto section = new QuestionSectionDto();
        section.setName("Section1");
        section.setQuestions(questionDtos);
        questionResponse.add(section);

        return questionResponse;
    }

    private QuestionDto mapFromQuestion(Question q) {
        QuestionDto qDto = new QuestionDto();
        qDto.setId(q.getId());
        qDto.setText(q.getText());
        qDto.setQuestionType("radio");
        if(!q.getOptions().isEmpty()) {
            QuestionOptions opt = q.getOptions().get(0);
            String[] options = opt.getOption().split(",");
            for(String option : options) {
                qDto.getOptions().add(option);
            }
        }
        return qDto;
    }
}
