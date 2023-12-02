package com.nick.propws.service;

import com.nick.propws.dto.AnswerDto;
import com.nick.propws.entity.MasterAnswer;
import com.nick.propws.exceptions.PropSheetException;

import java.util.List;

public interface MasterAnswerService {

    List<MasterAnswer> addMasterAnswer(List<AnswerDto> answer) throws PropSheetException;
}
