package com.nick.propws.service;

import com.nick.propws.dto.ShareGroupRequest;
import com.nick.propws.exceptions.PropSheetException;
import org.springframework.http.ResponseEntity;

public interface NotificationService {


    ResponseEntity<String> sendText(ShareGroupRequest req) throws PropSheetException;

    ResponseEntity<String> sendEmail(ShareGroupRequest req) throws PropSheetException;


}
