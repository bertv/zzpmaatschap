package com.bv.zzpmaatschap.services;


public class SMTPAuthenticator extends javax.mail.Authenticator {
    String SMTP_AUTH_USER = System.getProperty("SENDGRID_USERNAME");
    String SMTP_AUTH_PWD = System.getProperty("SENDGRID_PASSWORD");

    public javax.mail.PasswordAuthentication getPasswordAuthentication() {
        String username = SMTP_AUTH_USER;
        String password = SMTP_AUTH_PWD;
        return new javax.mail.PasswordAuthentication(username, password);
    }
}
