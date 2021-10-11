package dev.valium.sweetmeme.infra.email;

public interface EmailService {
    void send(EmailMessage emailMessage);
}
