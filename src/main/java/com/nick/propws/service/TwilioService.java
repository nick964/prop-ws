package com.nick.propws.service;

import com.nick.propws.dto.CreateGroupReq;
import com.nick.propws.dto.CreateGroupResponse;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.User;
import com.nick.propws.exceptions.PropSheetException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TwilioService {


    ResponseEntity<String> sendText(String number, String groupId);

    ResponseEntity<String> sendEmail(String number, String groupId);


}
