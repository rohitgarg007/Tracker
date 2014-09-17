package com.example.classes;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendMailClass {
    
    public void SendingMail(String to,String from,String sub,String text) 
    {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.port", "587");  
        properties.setProperty("mail.smtp.socketFactory.port", "587");  
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");  
        properties.setProperty("mail.smtp.starttls.enable", "true");  
        properties.setProperty("mail.smtp.auth", "true"); 
        //properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("aman.singhal1804","miku1804");
            }
        });

        try {
            session.setDebug(true);  
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress("Aman Singhal" + "<" +from+ ">"));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                    to));

            message.setSubject(sub);

            message.setText(text);

            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }    
    }

}
