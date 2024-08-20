package com.example.backend.controller;

import com.example.backend.dto.request.FormLogin;
import com.example.backend.dto.request.FormRegister;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.dto.response.ResponseUser;
import com.example.backend.model.entity.ResetPasswordRequest;
import com.example.backend.model.entity.User;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.security.principals.CustomUserDetailsService;
import com.example.backend.service.impl.UserServiceImpl;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {
    private static final String CLIENT_ID = "433942247848-u49shd599e24tin5b8i73en937r8tlob.apps.googleusercontent.com";
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
    @PostMapping("/public/google-login")
    public ResponseEntity<?> google(@RequestBody Map<String, String> request) throws GeneralSecurityException, IOException, ParseException {
        String token = request.get("token");
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
        try {
            System.out.println(token);
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // Kiểm tra xem người dùng đã tồn tại trong hệ thống hay chưa
                User user = userService.findByEmail(email);
                if (user == null) {
                    user = userService.registerOnlyEmail(email, name);
                }
                return new ResponseEntity<>(userService.loginByUser(user), HttpStatus.OK);
            } else {
                return ResponseEntity.status(401).body("Invalid ID token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Token verification failed.");
        }
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
        String resetPasswordLink = "https://app.qnsk.site/reset-password?token=" + token;
        try {
            sendEmail(email, resetPasswordLink, user.getUsername());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi khi gửi email!");
        }

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    private void sendEmail(String email, String resetPasswordLink, String username) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String subject = "Khôi phục mật khẩu";
        String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                + "<h2 style='text-align: center; color: #007bff; font-size: 24px;'>Khôi phục mật khẩu</h2>"
                + "<p style='font-size: 16px; color: #555;'>Chào " + username + ",</p>"
                + "<p style='font-size: 16px; color: #555;'>Bạn đã yêu cầu khôi phục mật khẩu. Vui lòng nhấn vào nút dưới đây để đặt lại mật khẩu của bạn:</p>"
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "<a href=\"" + resetPasswordLink + "\" style='display: inline-block; padding: 12px 24px; font-size: 16px; color: white; background-color: #007bff; border-radius: 5px; text-decoration: none;'>"
                + "Đặt lại mật khẩu"
                + "</a>"
                + "</div>"
                + "<p style='font-size: 16px; color: #555;'>Nếu bạn không yêu cầu khôi phục mật khẩu, vui lòng bỏ qua email này.</p>"
                + "<p style='font-size: 16px; color: #555;'>Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi!</p>"
                + "<p style='font-size: 16px; color: #555;'>Trân trọng,</p>"
                + "<p style='font-size: 16px; color: #555;'><strong>Đội ngũ Hỗ trợ - QNSK Team</strong></p>"
                + "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'/>"
                + "<p style='font-size: 12px; color: #777; text-align: center;'>Đây là email tự động, vui lòng không trả lời trực tiếp vào email này.</p>"
                + "</div>";


        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @PostMapping("/public/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        String token = request.getToken();
        String newPassword = request.getNewPassword();
        User user = userService.findByResetToken(token);
        if (user == null) {
            throw new RuntimeException("Token đã hết hạn");
        }

        // Mã hóa mật khẩu trước khi lưu
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetToken(null);
        userService.save(user);

        return "Mật khẩu đã được đặt lại thành công!";
    }

    @PostMapping("/public/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String responseUser =  userService.verifyAccount(email, otp);
        return new ResponseEntity<>(responseUser, HttpStatus.OK);
    }

}
