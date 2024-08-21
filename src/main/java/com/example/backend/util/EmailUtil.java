package com.example.backend.util;

import com.example.backend.dto.TransactionInfoDto;
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
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
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
        String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                + "<h2 style='text-align: center; color: #007bff; font-size: 24px;'>Xác Thực Tài Khoản</h2>"
                + "<p style='font-size: 16px; color: #555;'>Xin chào " + username + ",</p>"
                + "<p style='font-size: 16px; color: #555;'>Cảm ơn bạn đã đăng ký tài khoản tại QNSK!</p>"
                + "<p style='font-size: 16px; color: #555;'>Để hoàn tất quá trình đăng ký và kích hoạt tài khoản của bạn, vui lòng nhấp vào nút dưới đây để xác thực địa chỉ email của bạn:</p>"
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "<a href=\"https://app.qnsk.site/verify-account?email=" + email + "&otp=" + otp + "\" style='display: inline-block; padding: 12px 24px; font-size: 16px; color: white; background-color: #007bff; border-radius: 5px; text-decoration: none;'>"
                + "Xác Thực Tài Khoản"
                + "</a>"
                + "</div>"
                + "<p style='font-size: 16px; color: #555;'>Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email này và không có hành động nào khác cần thiết.</p>"
                + "<p style='font-size: 16px; color: #555;'>Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi!</p>"
                + "<p style='font-size: 16px; color: #555;'>Trân trọng,</p>"
                + "<p style='font-size: 16px; color: #555;'><strong>Đội ngũ Hỗ trợ - QNSK Team</strong></p>"
                + "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'/>"
                + "<p style='font-size: 12px; color: #777; text-align: center;'>Đây là email tự động, vui lòng không trả lời trực tiếp vào email này.</p>"
                + "</div>";

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);

        javaMailSender.send(mimeMessage);
    }


    private boolean isLastDayOfMonth(LocalDate date) {
        // Kiểm tra nếu ngày hôm nay là ngày cuối cùng của tháng
        return date.equals(date.withDayOfMonth(date.lengthOfMonth()));
    }

    private boolean isLastDayOfWeek(LocalDate date) {
        // Kiểm tra nếu ngày hôm nay là Chủ nhật (ngày cuối cùng của tuần)
        return date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private void sendEmail(String email,
                           String content,
                           List<TransactionInfoDto> transactionInfoDtos,
                           BigDecimal totalInitialAmount,
                           BigDecimal totalRemainingAmount,
                           MimeMessage mimeMessage,
                           MimeMessageHelper mimeMessageHelper) throws MessagingException {
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Báo cao thu thu tri");
        mimeMessageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);
        System.out.println("Email sent successfully");
    }


    @Scheduled(cron = "0 0 10 ? * SUN")
    public void sendWeeklyEmail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");


        LocalDate sunday = LocalDate.now();
        // Lấy ngày Thứ Hai của tuần hiện tại
        LocalDate monday = sunday.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // Lấy ngày Chủ Nhật của tuần hiện tại
//        LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        if (!isLastDayOfMonth(sunday)) {
            List<User> users = userRepository.findAllByIsActiveAndStatus();
            for (User user : users) {
                List<TransactionInfoDto> transactionInfoDtos = transactionRepository.findTransactionByUserIdBetweenStartDateAndEndDate(user.getId(), monday, sunday);
                if (transactionInfoDtos.size() > 0) {
                    Amount amount = getTotalAmount(transactionInfoDtos);
                }
            }

            mimeMessageHelper.setTo("phamtienquang57@gmail.com");
            mimeMessageHelper.setSubject("Gửi email tu dong");
            mimeMessageHelper.setText("<div>Day la tong thu ngay hom nay cua ban:</div>", true);
            javaMailSender.send(mimeMessage);
            System.out.println("Email sent successfully");
        }

    }

    private String emailContent(User user, TransactionInfoDto t, Amount amount){
        return """
                <div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>
                <p style='font-size: 16px; color: #555;'>Xin chào %s </p>
                
                </div>
                """;
    }

    private Amount getTotalAmount(List<TransactionInfoDto> t) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalOutcome = BigDecimal.ZERO;
        if (t != null) {
            for (TransactionInfoDto transactionInfoDto : t) {
                if (transactionInfoDto.getCategoryType() == 1) {
                    totalIncome = totalIncome.add(transactionInfoDto.getAmount());
                }
                if (transactionInfoDto.getCategoryType() == 0) {
                    totalOutcome = totalOutcome.add(transactionInfoDto.getAmount());
                }
                total.add(transactionInfoDto.getAmount());
            }
            Amount amount = new Amount(total, total.add(totalOutcome).subtract(totalIncome));
            return amount;
        }
        return null;
    }


    static class Amount {
        private BigDecimal totalInitialAmount;
        private BigDecimal totalRemainingAmount;

        public Amount() {
        }

        public Amount(BigDecimal totalInitialAmount, BigDecimal totalRemainingAmount) {
            this.totalInitialAmount = totalInitialAmount;
            this.totalRemainingAmount = totalRemainingAmount;
        }

        public BigDecimal getTotalInitialAmount() {
            return totalInitialAmount;
        }

        public void setTotalInitialAmount(BigDecimal totalInitialAmount) {
            this.totalInitialAmount = totalInitialAmount;
        }

        public BigDecimal getTotalRemainingAmount() {
            return totalRemainingAmount;
        }

        public void setTotalRemainingAmount(BigDecimal totalRemainingAmount) {
            this.totalRemainingAmount = totalRemainingAmount;
        }

    }

    @Scheduled(cron = "0 0 10 L * ?") // gui vao 10 gio sang trong ngay cuoi cua thang
    public void sendMonthlyEmail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        mimeMessageHelper.setTo("phamtienquang57@gmail.com");
        mimeMessageHelper.setSubject("Báo cao thu thu tri");
        mimeMessageHelper.setText("<div>Day la tong thu ngay hom nay cua ban:</div>", true);
        javaMailSender.send(mimeMessage);
        System.out.println("Email sent successfully");
    }


}
