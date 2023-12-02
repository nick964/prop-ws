package com.nick.propws.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TwilioServiceImpl implements TwilioService{

    @Value("${twilio.sid}")
    String twilioSid;

    @Value("${twilio.auth}")
    String twilioAuth;

    @Value("${twilio.phonenumber}")
    String twilioNumber;

    @Override
    public ResponseEntity<String> sendText(String number, String groupId) {
        initTwilio();
        Message.creator(new PhoneNumber(number), new PhoneNumber(twilioNumber), "We are working").create();
        return new ResponseEntity<>("Message sent successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> sendEmail(String number, String groupId) {
        return null;
    }


    private void initTwilio() {
        Twilio.init(twilioSid,twilioAuth);
    }
}
