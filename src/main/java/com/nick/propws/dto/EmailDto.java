package com.nick.propws.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmailDto {

    public EmailDto() {
        this.to = new ArrayList<>();
    }

    private String api_key;

    private List<String> to;

    private String sender;

    private String template_id;

    private EmailTemplateDto template_data;
    

}
