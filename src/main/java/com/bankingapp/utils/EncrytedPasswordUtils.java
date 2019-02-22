package com.bankingapp.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncrytedPasswordUtils {

    public static String encrptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public static void main(String[] args) {

        String password = "manoj";
        String encryptedPassword = encrptPassword(password);

        System.out.print("Encrypted password "+encryptedPassword);
    }
}
