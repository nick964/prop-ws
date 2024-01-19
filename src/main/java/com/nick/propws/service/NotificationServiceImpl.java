package com.nick.propws.service;


import com.nick.propws.dto.EmailDto;
import com.nick.propws.dto.EmailTemplateDto;
import com.nick.propws.dto.ShareGroupRequest;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
import com.nick.propws.exceptions.PropSheetException;
import com.nick.propws.repository.GroupRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Value("${smtp2go.sender}")
    String smtpSender;

    @Value("${smtp2go.deployedhost}")
    String deployedHost;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public ResponseEntity<String> sendText(ShareGroupRequest req) {
        return sendNotification(req, true);
    }

    @Override
    public ResponseEntity<String> sendEmail(ShareGroupRequest req) throws PropSheetException {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Group g = fetchGroup(req.getGroupId());

            List<Member> members =  g.getMembers().stream().filter(Member::isGroupAdmin).toList();
            String name = "";
            if(!members.isEmpty()) {
                name = members.get(0).getUser().getName();
            }

            String subject = "You have been invited to join a Superbowl Pool on superbowlproptracker.com!";

            String body =  "You have been invited to join the group " + g.getName() + " on Super Bowl Prop Tracker! " +
                    "Please sign up using this link: " + deployedHost + g.getId();

            if(name != null && !name.isEmpty()) {
                body+= "\n" + name + " is currently running this group.";
            }

            helper.setTo(req.getRecipient());
            helper.setSubject(subject);
            helper.setText(body, true); // 'true' to indicate HTML content
            helper.setFrom(smtpSender);

            emailSender.send(message);

            return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
        } catch (MessagingException e) {
            return new ResponseEntity<>("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<String> sendNotification(ShareGroupRequest req, boolean isSMS) {
        try {
            String inviteType = isSMS ? "text" : "email";
            System.out.println("Sending " + inviteType + " invite to " + req.getRecipient());
            Group g = fetchGroup(req.getGroupId());

            List<Member> members =  g.getMembers().stream().filter(Member::isGroupAdmin).toList();

            if(!members.isEmpty()) {
                String name = members.get(0).getUser().getName();
            }

            HttpHeaders headers = createHeaders();
            HttpEntity<EmailDto> requestEntity = getEmailDtoHttpEntity(req, g, headers, isSMS);

            //return sendRequestAndHandleResponse(requestEntity);
            return null;
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
        emailRequest.setSender(smtpSender);

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


}
