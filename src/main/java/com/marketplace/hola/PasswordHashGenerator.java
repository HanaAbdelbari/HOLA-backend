package com.marketplace.hola;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "hayola@3678#";

        String hash = encoder.encode(password);

        System.out.println("Password Hash:");
        System.out.println(hash);
    }
}