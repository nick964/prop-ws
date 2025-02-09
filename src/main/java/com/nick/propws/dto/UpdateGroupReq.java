package com.nick.propws.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateGroupReq {


    public Long id;
    public String name;
    public MultipartFile icon;
    public String description;
    public String venmoLink;
    public Integer groupCost;

}
