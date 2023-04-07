package com.haiyue.messaging.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {
    public static String passwordEncoder(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes());
            byte[] bytes= md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b: bytes){
                sb.append(String.format("%02x", (int) b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException();
        }
    }

    public static boolean verifyPassword(String password, String encodedPassword){
        String hashedPassword = passwordEncoder(password);
        return hashedPassword.equals(encodedPassword);
    }
}
