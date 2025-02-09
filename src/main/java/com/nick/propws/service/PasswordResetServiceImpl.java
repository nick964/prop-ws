package com.nick.propws.service;

import com.nick.propws.dto.OperationResponseDto;
import com.nick.propws.entity.PasswordResetToken;
import com.nick.propws.entity.User;
import com.nick.propws.exceptions.PropSheetException;
import com.nick.propws.repository.PasswordResetTokenRepository;
import com.nick.propws.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${running.host}")
    private String hostUrl;

    public void generateResetToken(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new PropSheetException("User not found with email: " + email);
        }

        if(!Objects.equals(user.getProvider(), "credentials")) {
            throw new PropSheetException("You didn't create an account on our site, looks like you signed up through this provider: " + user.getProvider());
        }

        //delete existing token if prsent
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(60)); // 30-minute expiry

        tokenRepository.save(resetToken);

        sendResetEmail(user.getEmail(), token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new PropSheetException("Invalid or expired token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new PropSheetException("Invalid or expired token");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken); // Clean up token
    }

    private void sendResetEmail(String email, String token) {
        try {
            String resetUrl = hostUrl + token;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Password Reset Request");
            message.setText("Click the link below to reset your password:\n" + resetUrl);
            message.setFrom("nick@superbowlproptracker.com");
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending email: ", e);
            throw new PropSheetException("Error sending email: " + e.getMessage());
        }

    }
}
