package com.nick.propws.controller;

import com.nick.propws.exceptions.ApiError;
import com.nick.propws.exceptions.PropSheetException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@ControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(value = PropSheetException.class)
    public ResponseEntity<ApiError> handlePropSheetException(PropSheetException ex) {
        ApiError error = new ApiError(400, ex.getLocalizedMessage(), new Date());
        return new ResponseEntity<ApiError>(error, HttpStatus.BAD_REQUEST);
    }
}
