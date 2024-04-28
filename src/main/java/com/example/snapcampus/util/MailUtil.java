package com.example.snapcampus.util;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.example.snapcampus.config.aws.AwsSesConfig;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {
    private static AwsSesConfig awsSesConfig = null;

    public MailUtil(AwsSesConfig awsSesConfig) {
        MailUtil.awsSesConfig = awsSesConfig;
    }

    public static void send(SenderDto senderDto) {
        try {
            AmazonSimpleEmailService client = awsSesConfig.amazonSimpleEmailService();
            client.sendEmail(senderDto.toSendRequestDto());
        } catch (Exception ex) {
            throw new IllegalArgumentException("이메일 전송 서비스가 원활하지 않습니다.");
        }
    }
}
