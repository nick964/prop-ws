package com.nick.propws.service;

import com.nick.propws.dto.EmailDto;
import com.nick.propws.dto.EmailTemplateDto;
import com.nick.propws.dto.ShareGroupRequest;
import com.nick.propws.entity.Group;
import com.nick.propws.exceptions.PropSheetException;
import com.nick.propws.repository.GroupRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TwilioServiceImpl implements TwilioService{

    @Value("${smtp2go.host}")
    String smtpHost;

    @Value("${smtp2go.apikey}")
    String smtpApiKey;

    @Value("${smtp2go.templateid}")
    String templateid;

    @Value("${smtp2go.sender}")
    String smtpSender;

    @Value("${smtp2go.textpost}")
    String textpost;

    @Value("${smtp2go.deployedhost}")
    String deployedHost;

    @Autowired
    GroupRepository groupRepository;

    @Override
    public ResponseEntity<String> sendText(ShareGroupRequest req) {
        return sendNotification(req, true);
    }

    @Override
    public ResponseEntity<String> sendEmail(ShareGroupRequest req) throws PropSheetException {
        return sendNotification(req, false);
    }

    private ResponseEntity<String> sendNotification(ShareGroupRequest req, boolean isSMS) {
        try {
            String inviteType = isSMS ? "text" : "email";
            System.out.println("Sending " + inviteType + " invite to " + req.getRecipient());
            Group g = fetchGroup(req.getGroupId());

            HttpHeaders headers = createHeaders();
            HttpEntity<EmailDto> requestEntity = getEmailDtoHttpEntity(req, g, headers, isSMS);

            return sendRequestAndHandleResponse(requestEntity);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send " + (isSMS ? "text" : "email"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        return headers;
    }

    private HttpEntity<EmailDto> getEmailDtoHttpEntity(ShareGroupRequest req, Group group, HttpHeaders headers, boolean isSMS) {
        EmailDto emailRequest = new EmailDto();
        emailRequest.setApi_key(smtpApiKey);
        emailRequest.setSender(smtpSender);
        if(isSMS) {
            String toValue = "1" + req.getRecipient() + textpost;
            emailRequest.getTo().add(toValue);
        } else {
            emailRequest.getTo().add(req.getRecipient());
        }
        emailRequest.setTemplate_id(templateid);
        EmailTemplateDto templateDetails = new EmailTemplateDto();
        templateDetails.setFirst_name(req.getName());
        templateDetails.setLink_value(deployedHost + group.getId());
        templateDetails.setGroup_name(group.getName());
        emailRequest.setTemplate_data(templateDetails);

        // Create request entity
        HttpEntity<EmailDto> requestEntity = new HttpEntity<>(emailRequest, headers);
        return requestEntity;
    }

    private Group fetchGroup(String groupId) {
        Long id = Long.parseLong(groupId);
        Group g = groupRepository.findGroupById(id);
        if(g == null) {
            throw new PropSheetException("Group not found with Group ID: " + id);
        }
        return g;
    }

    private ResponseEntity<String> sendRequestAndHandleResponse(HttpEntity<EmailDto> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(smtpHost, HttpMethod.POST, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
