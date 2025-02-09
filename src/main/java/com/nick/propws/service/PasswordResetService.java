package com.nick.propws.service;

public interface PasswordResetService {

    public void generateResetToken(String email);

    public void resetPassword(String token, String newPassword);

}
