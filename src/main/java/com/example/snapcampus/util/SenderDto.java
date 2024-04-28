package com.example.snapcampus.util;

import com.amazonaws.services.simpleemail.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SenderDto {
    private String from = "스냅캠퍼스 <snapcampus@abz.kr>";
    private ArrayList<String> to;
    private String subject;
    private String content;

    public SenderDto(ArrayList<String> to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public void addTo(String email) {
        to.add(email);
    }

    public SendEmailRequest toSendRequestDto() {
        Destination destination = new Destination().withToAddresses(to);
        Message message = new Message()
                .withSubject(createContent(subject))
                .withBody(new Body().withHtml(createContent(content)));
        return new SendEmailRequest()
                .withSource(from)
                .withDestination(destination)
                .withMessage(message);
    }

    private Content createContent(String text) {
        return new Content().withCharset("UTF-8").withData(text);
    }
}
