package com.nick.propws.service;

import com.nick.propws.dto.AnswerDto;
import com.nick.propws.entity.MasterAnswer;

public interface MasterAnswerService {

    MasterAnswer addMasterAnswer(AnswerDto answer);
}
