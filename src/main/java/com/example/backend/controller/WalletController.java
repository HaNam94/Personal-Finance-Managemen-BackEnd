package com.example.backend.controller;

import com.example.backend.dto.UserDto;
import com.example.backend.dto.WalletDto;
import com.example.backend.dto.WalletInfoDto;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.model.WalletRole;
import com.example.backend.model.entity.Wallet;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.IUserService;
import com.example.backend.service.IWalletService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final IWalletService walletService;
    private final IUserService userService;
    @Autowired
    private JavaMailSender mailSender;


    @GetMapping("")
    public ResponseEntity<?> getAllWallets(
            Authentication authentication
    ) {
        UserDto userDto = getUserDto(authentication);
        return new ResponseEntity<>(walletService.findAllWalletByUserId(userDto.getId()), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createWallet(
            Authentication authentication,
            @Validated @RequestBody WalletDto walletDto
    ) {
        UserDto userDto = getUserDto(authentication);
        walletService.saveWallet(userDto.getId(), walletDto);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWallet(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UserDto userDto = getUserDto(authentication);
        WalletInfoDto walletDto = walletService.getWalletWithPermission(id, userDto.getId());
        if (walletDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(walletDto, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWallet(
            @PathVariable Long id,
            @Validated @RequestBody WalletDto walletDto,
            Authentication authentication
    ) {
        UserDto userDto = getUserDto(authentication);
        if (!walletService.isOwner(id, userDto.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        walletService.updateWallet(id, walletDto);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @PutMapping("/{id}/{newAmount}")
    public ResponseEntity<?> updateWalletAmount(@PathVariable Long id, @PathVariable BigDecimal newAmount ) {
        walletService.updateWalletAmount(id,newAmount);
        return ResponseEntity.ok().build();
    }



    @DeleteMapping("/{walletId}")
    public ResponseEntity<?> deleteWallet(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UserDto userDto = getUserDto(authentication);
        if (!walletService.isOwner(id, userDto.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        walletService.deleteWalletById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/share-wallet/{id}")
    public ResponseEntity<?> shareWallet(@PathVariable("id") Long walletId,
                                         Authentication authentication,
                                         @RequestParam("email") String email,
                                         @RequestParam("role") String walletRoleName) {
        try {
            UserDto userDto = getUserDto(authentication);
            walletService.shareWallet(walletId, email, walletRoleName);
            WalletInfoDto walletDto = walletService.getWalletWithPermission(walletId, userDto.getId());
            sendEmail(email);
            return new ResponseEntity<>(walletDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi xảy ra: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    private UserDto getUserDto(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userService.findUserByEmail(userDetails.getUsername());
    }

    @DeleteMapping("/share-wallet/{walletId}")
    public ResponseEntity<?> removeWalletShare(
            @PathVariable Long walletId,
            @RequestParam String email) {

        try {
            walletService.removeWalletShare(walletId, email);
            return ResponseEntity.ok().body("Tài khoản đã được xóa khỏi danh sách chia sẻ.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/share-wallet/{id}")
    public ResponseEntity<?> updateWalletRole(@PathVariable("id") Long walletId,
                                              @RequestParam("userId") Long userId,
                                              @RequestParam("role") WalletRole walletRoleName) {
        try {
            System.out.println("walletId: " + walletId);
            System.out.println("userId: " + userId);
            System.out.println("role: " + walletRoleName);
            walletService.updateWalletRole(walletId, userId, walletRoleName);
            return new ResponseEntity<>("Cập nhật quyền thành công", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi xảy ra: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/transfer/{fromWalletId}/{toWalletId}/{amount}")
    public ResponseEntity<String> transferMoney(
            @PathVariable(value = "fromWalletId", required = false) Long fromWalletId,
            @PathVariable(value = "toWalletId", required = false) Long toWalletId,
            @PathVariable(value = "amount", required = false) BigDecimal amount) {

        walletService.transferMoney(fromWalletId, toWalletId, amount);
        return ResponseEntity.ok("Transfer successful");
    }
    private void sendEmail(String email) throws  MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String subject = "Thông Báo: Bạn Đã Được Chia Sẻ Ví";

        String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                + "<h2 style='text-align: center; color: #007bff; font-size: 24px;'>Thông Báo Chia Sẻ Ví</h2>"
                + "<p style='font-size: 16px; color: #555;'>Xin chào,</p>"
                + "<p style='font-size: 16px; color: #555;'>Chúng tôi rất vui mừng thông báo rằng bạn đã được chia sẻ một ví trong hệ thống của chúng tôi. Đây là một cơ hội tuyệt vời để bạn quản lý tài chính của mình một cách hiệu quả hơn.</p>"
                + "<p style='font-size: 16px; color: #555;'>Nếu bạn có bất kỳ thắc mắc nào, xin vui lòng liên hệ với chúng tôi qua email hoặc số điện thoại hỗ trợ.</p>"
                + "<p style='font-size: 16px; color: #555;'>Trân trọng,</p>"
                + "<p style='font-size: 16px; color: #555;'><strong>Đội ngũ Hỗ trợ</strong></p>"
                + "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'/>"
                + "<p style='font-size: 12px; color: #777; text-align: center;'>Đây là email tự động, vui lòng không trả lời trực tiếp vào email này.</p>"
                + "</div>";

        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content,true);
        mailSender.send(message);
    }

}
