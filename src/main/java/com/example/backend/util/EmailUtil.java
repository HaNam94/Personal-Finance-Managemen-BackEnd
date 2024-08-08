package com.example.backend.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

  @Autowired
  private JavaMailSender javaMailSender;

  public void sendOtpEmail(String email, String otp, String username) throws MessagingException {

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,"UTF-8");

    mimeMessageHelper.setTo(email);

    mimeMessageHelper.setSubject("Verify OTP");

    mimeMessageHelper.setText("""
        <div>
        <p>Xin chào %s</p>
                            
                           <p> Cảm ơn bạn đã đăng ký tài khoản tại [Tên Công Ty/Dịch Vụ]!</p>
                            
                            <p>Để hoàn tất quá trình đăng ký và kích hoạt tài khoản của bạn, vui lòng nhấp vào liên kết dưới đây để xác thực địa chỉ email của bạn:</p>
                            
                            <p><a href="http://localhost:3000/verify-account?email=%s&otp=%s" target="_blank">Xác Thực Tài Khoản</a></p>
                            
                            <p> Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email này và không có hành động nào khác cần thiết.</p>
                           
                            <p>Cảm ơn bạn.</p>
                            
          
        </div>
        """.formatted(username,email, otp), true);
// gui den email da duoc thiet lap
    javaMailSender.send(mimeMessage);
  }
}
