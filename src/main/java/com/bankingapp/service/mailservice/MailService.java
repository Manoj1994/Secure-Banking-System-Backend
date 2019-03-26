/*
Set up email information at application.properties
 */

package com.bankingapp.service.mailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendOTPMessage(String email, String otp) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeHelper;

        try {
            mimeHelper = new MimeMessageHelper(mimeMessage, true);

            String otpMessage = "Your OTP Password is :"+otp;
            mimeHelper.setSubject("OTP Verification");
            mimeHelper.setTo(email);
            mimeHelper.setText(otpMessage);

            javaMailSender.send(mimeMessage);
            return true;

        } catch (Exception e) {

            return false;
        }
    }
}
