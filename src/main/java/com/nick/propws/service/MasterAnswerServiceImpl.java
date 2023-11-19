package com.nick.propws.service;

import com.nick.propws.dto.AnswerDto;
import com.nick.propws.entity.MasterAnswer;
import com.nick.propws.repository.MasterAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
public class MasterAnswerServiceImpl implements MasterAnswerService{

    @Autowired
    MasterAnswerRepository masterAnswerRepository;
    @Override
    public MasterAnswer addMasterAnswer(AnswerDto answer) {
        log.info("Adding new master answer: " + answer.toString());



        return null;
    }
}
