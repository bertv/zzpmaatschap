package com.bv.passwordgenerator;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by bertv on 10-1-14.
 */
public class PasswordGenerator {

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String password = "1234567890";
        MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes("UTF-8"));
        byte[] passwordDigest = md.digest();
        String encodedPasswordHash = new sun.misc.BASE64Encoder().encode(passwordDigest);
        System.out.println(encodedPasswordHash);
    }
}
