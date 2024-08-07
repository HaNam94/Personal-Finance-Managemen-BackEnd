package com.example.backend.controller;

import com.example.backend.dto.request.FormLogin;
import com.example.backend.dto.request.FormRegister;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.dto.response.ResponseUser;
import com.example.backend.model.entity.ResetPasswordRequest;
import com.example.backend.model.entity.User;
import com.example.backend.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;


    @PostMapping("/public/login")
    public ResponseEntity<?> login(@Valid @RequestBody FormLogin formLogin,
                             BindingResult bindingResult) {
      ResponseUser responseUser =  userService.login(formLogin, bindingResult);
        return new ResponseEntity<>(responseUser, HttpStatus.OK);
    }
    @PostMapping("/public/register")
    public ResponseEntity<?> register(@Valid @RequestBody FormRegister formRegister, BindingResult result) {
        ResponseSuccess responseSuccess = userService.register(formRegister, result);
            return new ResponseEntity<>(responseSuccess, HttpStatus.OK);

    }
    @PutMapping("/public/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email,
                                                @RequestParam String otp) {
        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
    }
    @PutMapping("/public/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
    }
    @PostMapping("/public/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("Email không tồn tại!");
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userService.save(user);

        String resetPasswordLink = "http://localhost:3000/reset-password?token=" + token;
        sendEmail(email, resetPasswordLink);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    private void sendEmail(String email, String resetPasswordLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Khôi phục mật khẩu");
        message.setText("Nhấn vào liên kết dưới đây để đặt lại mật khẩu:\n" + resetPasswordLink);
        mailSender.send(message);
    }

    @PostMapping("/public/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        String token = request.getToken();
        String newPassword = request.getNewPassword();
        User user = userService.findByResetToken(token);
        if (user == null) {
            return "Token không hợp lệ!";
        }

        // Mã hóa mật khẩu trước khi lưu
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetToken(null);
        userService.save(user);

        return "Mật khẩu đã được đặt lại thành công!";
    }

}
