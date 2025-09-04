package com.ntp.be.auth.services;

import com.ntp.be.auth.entities.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public String sendMail(User user) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender, "ShopNTP");
            helper.setTo(user.getEmail());
            helper.setSubject("Verify your ShopNTP account");

            String content = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "  <meta charset='UTF-8'>" +
                    "  <style>" +
                    "    body { font-family: Arial, sans-serif; line-height: 1.6; }" +
                    "    .container { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 8px; }" +
                    "    h2 { color: #333; }" +
                    "    .code { font-size: 20px; font-weight: bold; color: #d9534f; background: #f9f9f9; padding: 10px; border-radius: 4px; display: inline-block; }" +
                    "    .footer { font-size: 12px; color: #777; margin-top: 30px; border-top: 1px solid #eee; padding-top: 10px; }" +
                    "  </style>" +
                    "</head>" +
                    "<body>" +
                    "  <div class='container'>" +
                    "    <h2>Hello " + user.getFirstName() + " " + user.getLastName() + ",</h2>" +
                    "    <p>Thank you for registering with <b>ShopNTP</b>.</p>" +
                    "    <p>Your email verification code is:</p>" +
                    "    <p class='code'>" + user.getVerificationCode() + "</p>" +
                    "    <p>Please enter this code to complete your registration.</p>" +
                    "    <p>If you did not request this registration, please ignore this email.</p>" +
                    "    <div class='footer'>" +
                    "      <p>Best regards,<br>The ShopNTP Team</p>" +
                    "    </div>" +
                    "  </div>" +
                    "</body>" +
                    "</html>";

            helper.setText(content, true);

            javaMailSender.send(message);
            return "Email sent";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail: " + e.getMessage();
        }
    }
}
