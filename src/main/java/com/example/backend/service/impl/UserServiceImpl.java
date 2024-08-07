package com.example.backend.service.impl;

import com.example.backend.dto.UserDto;
import com.example.backend.dto.request.FormLogin;
import com.example.backend.dto.request.FormRegister;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.dto.response.ResponseUser;
import com.example.backend.exception.CustomValidationException;
import com.example.backend.model.entity.Role;
import com.example.backend.model.entity.User;
import com.example.backend.repository.IRoleRepo;
import com.example.backend.repository.UserRepo;
import com.example.backend.security.jwt.JWTProvider;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.UserService;
import com.example.backend.util.EmailUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepo userRepository;
    private final IRoleRepo roleRepository;
//    private final UserGenericMapperImpl genericMapperImpl;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;
    private final EmailUtil emailUtil;



    @Override
    public ResponseSuccess register(FormRegister formRegister, BindingResult bindingResult) {

        Map<String, String> map = new HashMap<>();
        if (checkIsExistEmail(formRegister.getEmail())) {

            map.put("email", "Email da ton tai");

        }
        if (checkIsExistPhone(formRegister.getPhone())) {

            map.put("phone", "So dien thoai da ton tai");

        }
        if (!checkBirthdayBeforeEighteenYearAgo(formRegister.getDob())) {

            map.put("dob", "Ban chua du 18 tuoi");
        }
        if (bindingResult.hasErrors()) {
            for (FieldError err : bindingResult.getFieldErrors()) {
                map.put(err.getField(), err.getDefaultMessage());
            }
        }
        if (map.size() > 0) {
            throw new CustomValidationException(map);
        }
        String otpCode = generateRandomString(6);
        try {
            emailUtil.sendOtpEmail(formRegister.getEmail(), otpCode, formRegister.getUsername());
        } catch (MessagingException e) {
            map.put("otpCode", "Co loi trong qua trinh gui email");
            throw new CustomValidationException(map);
        }
        User user = User.builder()
                .username(formRegister.getUsername())
                .email(formRegister.getEmail())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .dob(formRegister.getDob())
                .phone(formRegister.getPhone())
                .isAccountGoogle(false)
                .isDelete(false)
                .userStatus(true)
                .isActive(false)
                .build();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findRolesByRoleName("ROLE_USER"));
        user.setRoles(roles);

        //        user.setOtpCode(generateUUID());
        user.setOtpCode(otpCode);
        userRepository.save(user);
        ResponseSuccess response = ResponseSuccess.builder()
                .message("DK OK")
                .build();
        return response;

    }

    public String verifyAccount(String email, String otp) {


        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
//        if (user.getOtpCode().equals(otp) && Duration.between(user.getOtpGenerateTime(),
//                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setIsActive(true);
            userRepository.save(user);
            return "OTP verified you can login";
//        }
//        return "Please regenerate otp and try again";
    }

    public String regenerateOtp(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = generateRandomString(6);

        try {
            emailUtil.sendOtpEmail(email, otp, user.getUsername());
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        user.setOtpCode(otp);
        user.setOtpGenerateTime(LocalDateTime.now());
        userRepository.save(user);
        return "Email sent... please verify account within 1 minute";
    }


    @Override
    public ResponseUser login(FormLogin formLogin, BindingResult bindingResult) {
        User user = findByEmail(formLogin.getEmail());
        Map<String, String> errMap = new HashMap<>();
        System.out.println(checkIsExistEmail(formLogin.getEmail()));
        if (!checkIsExistEmail(formLogin.getEmail())) {
            errMap.put("user", "Tai khoan khong ton tai");
            throw new CustomValidationException(errMap);
        }
        if (!user.getIsActive()){
            errMap.put("isActive", "Tai khoan chua active");
            throw new CustomValidationException(errMap);
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getEmail(), formLogin.getPassword()));

        } catch (AuthenticationException e) {
            errMap.put("emailAndPassword", "Email hoac Password khong dung");
            throw new CustomValidationException(errMap);
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = jwtProvider.generateAccessToken(userDetails);
        ResponseUser responseUser = ResponseUser.builder()
                .email(user.getEmail())
                .userStatus(user.getUserStatus())
                .phone(user.getPhone())
                .birthday(user.getDob())
                .avatar(user.getAvatar())
                .fullName(user.getUsername())
                .isDelete(user.getIsDelete())
                .authorities(userDetails.getAuthorities())
                .accessToken(accessToken)
                .build();
        return responseUser;
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findUserById(id).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }

    public Boolean checkIsExistEmail(String email) {
        User user = findByEmail(email);
        if (user == null) {
            return false;
        }
        return true;
    }

    public Boolean checkIsExistPhone(String phone) {
        User user = userRepository.findUserByPhone(phone).orElse(null);
        if (user == null) {
            return false;
        }
        return true;
    }

    public Boolean checkBirthdayBeforeEighteenYearAgo(LocalDate birthday) {

        LocalDate dateEighteenYearsAgo = LocalDate.now().minusYears(18);
        if (birthday.isBefore(dateEighteenYearsAgo)) {
            return true;
        }
        return false;
    }

    public Boolean checkEmailAndPassword(String email, String pass) {
        User user = userRepository.findUserByEmailAndPassword(email, pass).orElse(null);
        if (user == null) {
            return false;
        }
        return true;

    }

    public Boolean checkIsDelete(String email) {
        User user = findByEmail(email);
        return user.getIsDelete();
    }

    public  String generateUUID() {

        return UUID.randomUUID().toString();
    }

    public static String generateRandomString(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String CHARACTERS2 = "21984651689789167925315482100251002150005" +
                "48798620";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS2.length());
            sb.append(CHARACTERS2.charAt(index));
        }
        return sb.toString();
    }
}
