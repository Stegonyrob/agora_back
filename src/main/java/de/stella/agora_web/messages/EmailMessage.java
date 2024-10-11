package de.stella.agora_web.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailMessage {
    private String toAddress;
    private String subject;
    private String body;

    public EmailMessage(String toAddress, String subject, String body) {
        this.toAddress = toAddress;
        this.subject = subject;
        this.body = body;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}