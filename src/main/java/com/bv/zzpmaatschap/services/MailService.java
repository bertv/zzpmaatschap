package com.bv.zzpmaatschap.services;


import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Stateless
public class MailService {


    String SMTP_HOST_NAME = System.getProperty("SENDGRID_SMTP_HOST");

    public void mail(String content, String to, String subject) {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        Authenticator auth;
        Session mailSession;
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", "true");
        auth = new SMTPAuthenticator();
        mailSession = Session.getDefaultInstance(props, auth);

        mailSession.setDebug(true);
        MimeMessage message = new MimeMessage(mailSession);
        Transport transport = null;
        Multipart multipart = new MimeMultipart("alternative");
        try {
            transport = mailSession.getTransport();
            BodyPart part1 = new MimeBodyPart();
            part1.setText(content);

            BodyPart part2 = new MimeBodyPart();

            part2.setContent(content, "text/html");


            multipart.addBodyPart(part1);
            multipart.addBodyPart(part2);

            message.setContent(multipart);

            message.setFrom(new InternetAddress("info@zzpmaatschap.nl"));
            InternetAddress toAddress[] = new InternetAddress[1];
            toAddress[0] = new InternetAddress(to);
            message.setRecipients(Message.RecipientType.TO, toAddress);
            message.setSubject(subject);

            transport.connect();
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            if (transport != null)
                try {
                    transport.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
        }
    }

}
