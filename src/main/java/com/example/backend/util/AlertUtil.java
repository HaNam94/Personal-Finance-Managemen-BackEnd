package com.example.backend.util;

import com.example.backend.model.entity.User;
import com.example.backend.repository.ITransactionRepo;
import com.example.backend.repository.IUserRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AlertUtil {
    private final JavaMailSender javaMailSender;
    private final IUserRepo userRepository;
    private final ITransactionRepo transactionRepository;

    @Scheduled(cron = "0 0 7 * * *")
    public void sendTodayEmail() {
        List<User> users = userRepository.findAllByIsActiveAndStatus();
        users.forEach(user -> {
            System.out.println("Sending email to " + user.getEmail());
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = null;
            try {
                mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                String subject = "Ứng Dụng Quản Lý Tài Chính QNSK";
                String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                        + "<h2 style='text-align: center; color: #007bff; font-size: 24px;'>Chào Buổi Sáng!</h2>"
                        + "<p style='font-size: 16px; color: #555;'>Xin chào " + user.getUsername() + ",</p>"
                        + "<p style='font-size: 16px; color: #555;'>Chúc bạn có một ngày mới thật tươi sáng và đầy năng lượng! Đừng quên kiểm tra tình hình tài chính của mình hôm nay để đảm bảo mọi thứ đều trong tầm kiểm soát.</p>"
                        + "<p style='font-size: 16px; color: #555;'>Nếu cần hỗ trợ hoặc có bất kỳ thắc mắc nào, chúng tôi luôn sẵn sàng giúp đỡ.</p>"
                        + "<p style='font-size: 16px; color: #555;'>Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi!</p>"
                        + "<p style='font-size: 16px; color: #555;'>Trân trọng,</p>"
                        + "<p style='font-size: 16px; color: #555;'><strong>Đội ngũ Hỗ trợ - QNSK Team</strong></p>"
                        + "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'/>"
                        + "<p style='font-size: 12px; color: #777; text-align: center;'>Đây là email tự động, vui lòng không trả lời trực tiếp vào email này.</p>"
                        + "</div>";
                mimeMessageHelper.setTo(user.getEmail());
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(content, true);

                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

        });

    }

    @Scheduled(cron = "0 0 10,13,17,20 * * *")
    public void sendNoTransactionAlert() {
        List<User> users = userRepository.findAllByIsActiveAndStatus();
        users.forEach(user -> {
            if(transactionRepository.existsTransactionInDayByUserId(user.getId(), LocalDate.now())) {
                return;
            }
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = null;
            try {
                mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                String subject = "Ứng Dụng Quản Lý Tài Chính QNSK- Nhắc Nhở";
                String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                        + "<h2 style='text-align: center; color: #007bff; font-size: 24px;'>Thêm Giao Dịch</h2>"
                        + "<p style='font-size: 16px; color: #555;'>Chào " + user.getUsername() + ",</p>"
                        + "<p style='font-size: 16px; color: #555;'>Chúng tôi nhận thấy bạn chưa thêm bất kỳ giao dịch nào trong hôm nay.</p>"
                        + "<p style='font-size: 16px; color: #555;'>Để giúp bạn quản lý tài chính hiệu quả hơn, hãy thêm các giao dịch mới vào ứng dụng của chúng tôi.</p>"
                        + "<p style='font-size: 16px; color: #555;'>Nếu bạn cần hỗ trợ hoặc có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi.</p>"
                        + "<p style='font-size: 16px; color: #555;'>Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi!</p>"
                        + "<p style='font-size: 16px; color: #555;'>Trân trọng,</p>"
                        + "<p style='font-size: 16px; color: #555;'><strong>Đội ngũ Hỗ trợ - QNSK Team</strong></p>"
                        + "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'/>"
                        + "<p style='font-size: 12px; color: #777; text-align: center;'>Đây là email tự động, vui lòng không trả lời trực tiếp vào email này.</p>"
                        + "</div>";
                mimeMessageHelper.setTo(user.getEmail());
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(content, true);

                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

        });

    }
}
