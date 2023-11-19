package com.nick.propws.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class ApiError {

    public ApiError(Integer errorCode, String errorDesc, Date date) {
        super();
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.date = date;
    }

    private Integer errorCode;
    private String errorDesc;
    private Date date;


}
