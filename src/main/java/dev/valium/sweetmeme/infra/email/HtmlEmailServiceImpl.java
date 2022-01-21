package dev.valium.sweetmeme.infra.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 메일 인증 서비스 구현체
 */
@Slf4j
@Profile("dev, linux")
@Component
@RequiredArgsConstructor
public class HtmlEmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getContext(), true);
            javaMailSender.send(mimeMessage);
            log.info("sent email: {}", emailMessage.getContext());
        } catch (MessagingException e) {
            log.error("failed to send email", e);
            throw new RuntimeException(e);
        }
    }
}
