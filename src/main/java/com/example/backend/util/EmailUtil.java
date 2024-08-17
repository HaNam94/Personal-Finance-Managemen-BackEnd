package com.example.backend.util;

import com.example.backend.dto.UserDto;
import com.example.backend.model.entity.User;
import com.example.backend.repository.ITransactionRepo;
import com.example.backend.repository.IUserRepo;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.ITransactionService;
import com.example.backend.service.IUserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailUtil {


    private final JavaMailSender javaMailSender;
    private final ITransactionRepo transactionRepository;
    private final IUserRepo userRepository;


    public void sendOtpEmail(String email, String otp, String username) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String subject = "Verify OTP";
        String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                + "<div style='padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f8f9fa;'>"
                + "<h2 style='text-align: center; color: #007bff;'>Xác Thực Tài Khoản</h2>"
                + "<div style='border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); padding: 20px; background-color: white;'>"
                + "<p>Xin chào " + username + ",</p>"
                + "<p>Cảm ơn bạn đã đăng ký tài khoản tại [Tên Công Ty/Dịch Vụ]!</p>"
                + "<p>Để hoàn tất quá trình đăng ký và kích hoạt tài khoản của bạn, vui lòng nhấp vào nút dưới đây để xác thực địa chỉ email của bạn:</p>"
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "<a href=\"http://localhost:3000/verify-account?email=" + email + "&otp=" + otp + "\" style='display: inline-block; padding: 12px 24px; font-size: 16px; color: white; background-color: #007bff; border-radius: 5px; text-decoration: none;'>"
                + "Xác Thực Tài Khoản"
                + "</a>"
                + "</div>"
                + "<p>Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email này và không có hành động nào khác cần thiết.</p>"
                + "<p>Cảm ơn bạn.</p>"
                + "</div>"
                + "</div>"
                + "</div>";

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);

        javaMailSender.send(mimeMessage);
    }


//    private String contentEmail(){
//        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
//                + "<div style='padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f8f9fa;'>"
//                + "<h2 style='text-align: center; color: #007bff;'>Xác Thực Tài Khoản</h2>"
//                + "<div style='border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); padding: 20px; background-color: white;'>"
//                + "<p>Xin chào " + username + ",</p>"
//                + "<p>Cảm ơn bạn đã đăng ký tài khoản tại [Tên Công Ty/Dịch Vụ]!</p>"
//                + "<p>Để hoàn tất quá trình đăng ký và kích hoạt tài khoản của bạn, vui lòng nhấp vào nút dưới đây để xác thực địa chỉ email của bạn:</p>"
//                + "<div style='text-align: center; margin: 20px 0;'>"
//                + "<a href=\"http://localhost:3000/verify-account?email=" + email + "&otp=" + otp + "\" style='display: inline-block; padding: 12px 24px; font-size: 16px; color: white; background-color: #007bff; border-radius: 5px; text-decoration: none;'>"
//                + "Xác Thực Tài Khoản"
//                + "</a>"
//                + "</div>"
//                + "<p>Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email này và không có hành động nào khác cần thiết.</p>"
//                + "<p>Cảm ơn bạn.</p>"
//                + "</div>"
//                + "</div>"
//                + "</div>";
//    }


    private boolean isLastDayOfMonth(LocalDate date) {
        // Kiểm tra nếu ngày hôm nay là ngày cuối cùng của tháng
        return date.equals(date.withDayOfMonth(date.lengthOfMonth()));
    }

    private boolean isLastDayOfWeek(LocalDate date) {
        // Kiểm tra nếu ngày hôm nay là Chủ nhật (ngày cuối cùng của tuần)
        return date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private void sendEmail(String email,String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Báo cao thu thu tri");
        mimeMessageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);
        System.out.println("Email sent successfully");
    }



    @Scheduled(cron = "0 0 17 * * ?") // bao cao vao 17 gio moi ngay tru chu nhat
    public void sendTodayEmail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        LocalDate today = LocalDate.now();
        if (!isLastDayOfWeek(today)) {
            if (!isLastDayOfMonth(today)) {
                List<User> users = userRepository.findAllByIsActiveAndStatus();
                sendEmail("test", "test");
            }

        }

    }
}
