package com.nick.propws.controller;

import com.nick.propws.dto.OperationResponseDto;
import com.nick.propws.dto.PasswordResetDto;
import com.nick.propws.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("reset-password")
public class PasswordResetController {

    @Autowired
    private PasswordResetService   passwordResetService;

    @PostMapping("/request")
    public ResponseEntity<OperationResponseDto> requestPasswordReset(@RequestBody Map<String, String> request) {
        OperationResponseDto operationResponseDto = new OperationResponseDto();
        operationResponseDto.setStatus("SUCCESS");
        try {
            String email = request.get("email");
            passwordResetService.generateResetToken(email);
            operationResponseDto.setMessage("Password reset link sent if email exists.");
            return new ResponseEntity<>(operationResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            operationResponseDto.setStatus("ERROR");
            operationResponseDto.setMessage(e.getMessage());
            return new ResponseEntity<>(operationResponseDto, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<OperationResponseDto> resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        OperationResponseDto operationResponseDto = new OperationResponseDto();
        operationResponseDto.setStatus("SUCCESS");
        try {
            passwordResetService.resetPassword(passwordResetDto.getToken(), passwordResetDto.getNewPassword());
            operationResponseDto.setMessage("Password reset was successful.");
            return new ResponseEntity<>(operationResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            operationResponseDto.setStatus("ERROR");
            operationResponseDto.setMessage(e.getMessage());
            return new ResponseEntity<>(operationResponseDto, HttpStatus.OK);
        }
    }


}
