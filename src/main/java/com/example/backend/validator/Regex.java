package com.example.backend.validator;

public class Regex {
    public static final String REGEX_PASS= "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d@#$%^&+=]{6,32}$";
    public static final String REGEX_PHONE = "^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6|7|8|9]|8[1-5]|9[0-9])\\d{7}$";
}
