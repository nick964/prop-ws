package com.nick.propws.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateGroupReq {

    public String name;

    public MultipartFile icon;

    public String description;

}
