package com.example.backend.service.impl;

import com.example.backend.dto.request.FormLogin;
import com.example.backend.dto.request.FormRegister;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.dto.response.ResponseUser;
import com.example.backend.exception.CustomValidationException;
import com.example.backend.model.entity.Role;
import com.example.backend.model.entity.User;
import com.example.backend.repository.IRoleRepo;
import com.example.backend.repository.IUserRepo;
import com.example.backend.security.jwt.JWTProvider;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.IUserService;
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
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {
    private final IUserRepo userRepository;
    private final IRoleRepo roleRepository;
//    private final UserGenericMapperImpl genericMapperImpl;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;



    @Override
    public ResponseSuccess register(FormRegister formRegister, BindingResult bindingResult) {

        Map<String, String> map = new HashMap<>();
        if (checkIsExistEmail(formRegister.getEmail())) {

            map.put("email", "Email da ton tai");

        }
        if (checkIsExistPhone(formRegister.getPhone())) {

            map.put("password", "Password da ton tai");

        }
        if (!checkBirthdayBeforeEighteenYearAgo(formRegister.getDob())) {

            map.put("birthday", "Ban chua du 18 tuoi");
        }
        if (bindingResult.hasErrors()) {
            for (FieldError err : bindingResult.getFieldErrors()) {
                map.put(err.getField(), err.getDefaultMessage());
            }
        }
        if (map.size() > 0) {
            throw new CustomValidationException(map);
        }

//        User user = genericMapperImpl.formRegisterToEntity(formRegister);
        User user = User.builder()
                .username(formRegister.getEmail())
                .email(formRegister.getEmail())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .dob(formRegister.getDob())
                .phone(formRegister.getPhone())
                .isAccountGoogle(false)
                .isDelete(false)
                .userStatus(true)
                .build();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findRolesByRoleName("ROLE_USER"));
        user.setRoles(roles);
        //        user.setOtpCode(generateUUID());
        user.setOtpCode(generateRandomString(6));
        userRepository.save(user);
        ResponseSuccess response = ResponseSuccess.builder()
                .message("DK OK")
                .build();
        return response;

    }


    @Override
    public ResponseUser login(FormLogin formLogin, BindingResult bindingResult) {
        User user = findByEmail(formLogin.getEmail());
        Map<String, String> errMap = new HashMap<>();
        System.out.println(checkIsExistEmail(formLogin.getEmail()));
        if (!checkIsExistEmail(formLogin.getEmail())) {
            errMap.put("user", "Tai khoan da bi xoa");
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
