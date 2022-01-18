package dev.valium.sweetmeme.module.email;

import dev.valium.sweetmeme.infra.config.ApplicationProperties;
import dev.valium.sweetmeme.infra.email.EmailMessage;
import dev.valium.sweetmeme.infra.email.EmailService;
import dev.valium.sweetmeme.module.bases.BaseController;
import dev.valium.sweetmeme.module.email.form.EmailVerifyForm;
import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 회원가입 인증 email controller
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class EmailController extends BaseController {

    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final ApplicationProperties applicationProperties;
    private final MemberService memberService;
    private final MessageSource messageSource;

    @GetMapping("/email/verify")
    public String emailVerifyForm(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "emailVerify");
        model.addAttribute(new EmailVerifyForm());

        return "mail/emailVerify";
    }
    @PostMapping("/email/verify")
    public String emailVerify(@CurrentMember Member member, Model model,
                              @Valid EmailVerifyForm form, BindingResult result) {

        if(result.hasErrors()) {
            setBaseAttributes(member, model, "emailVerify");
            result.rejectValue("email", "email.verify.email-error", new Object[]{}, "등록된 이메일과 일치하지 않습니다.");
            return "mail/emailVerify";
        }

        return "redirect:/email/verify/sent";
    }

    @GetMapping("/email/verify/sent")
    public String emailSent(@CurrentMember Member member, HttpServletRequest request) {

        Context context = new Context();

        context.setVariable("link", "?token=" + member.getEmailCheckToken() + "&email=" + member.getEmail());
        context.setVariable("host", applicationProperties.getHost() + request.getContextPath());
        context.setLocale(request.getLocale());

        String message = templateEngine.process("mail/sentEmailVerifyForm", context);
        EmailMessage emailMessage = new EmailMessage(member.getEmail(), messageSource.getMessage("email.subject",
                null, "스윗밈 인증메일 입니다.", request.getLocale()), message);

        emailService.send(emailMessage);

        return "redirect:/email/verify/waiting";
    }

    @GetMapping("/email/verify/waiting")
    public String emailSentAndWaiting() {
        return "mail/emailSent";
    }

    @GetMapping("/email/verify/check")
    public String emailVerifyCheck(@RequestParam String token, @RequestParam String email) throws Exception {

        memberService.verifyEmail(token, email);

        return "mail/emailVerifyDone";
    }
}
