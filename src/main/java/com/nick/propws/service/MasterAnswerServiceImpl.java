package com.nick.propws.service;

import com.nick.propws.dto.AnswerDto;
import com.nick.propws.entity.MasterAnswer;
import com.nick.propws.entity.Question;
import com.nick.propws.exceptions.PropSheetException;
import com.nick.propws.repository.MasterAnswerRepository;
import com.nick.propws.repository.MemberAnswerRepository;
import com.nick.propws.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class MasterAnswerServiceImpl implements MasterAnswerService{

    @Autowired
    MasterAnswerRepository masterAnswerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    MemberAnswerRepository memberAnswerRepository;

    @Override
    public List<MasterAnswer> addMasterAnswer(List<AnswerDto> answers) throws PropSheetException {
        List<MasterAnswer> answersToReturn = new ArrayList<>();
        for(AnswerDto answer : answers) {
            log.info("Adding new master answer: " + answer.toString());
            MasterAnswer masterAnswer = new MasterAnswer();
            Optional<Question> q = questionRepository.findById(answer.getQuestionId());
            if(q.isEmpty()) {
                throw new PropSheetException("Error - no question found with id " + answer.getQuestionId());
            }
            masterAnswer.setQuestion(q.get());
            masterAnswer.setAnswer(answer.getAnswer());
            MasterAnswer answer1 = masterAnswerRepository.save(masterAnswer);
            memberAnswerRepository.updateScoresAfterSubmission(q.get().getId().intValue(), answer1.getId().intValue());
            answersToReturn.add(answer1);
        }
        return answersToReturn;

    }
}
