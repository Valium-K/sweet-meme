package dev.valium.sweetmeme.module.member;

import dev.valium.sweetmeme.infra.config.ApplicationProperties;
import dev.valium.sweetmeme.infra.email.EmailMessage;
import dev.valium.sweetmeme.infra.email.EmailService;
import dev.valium.sweetmeme.module.bases.BaseController;
import dev.valium.sweetmeme.module.member.form.ForgotPasswordForm;
import dev.valium.sweetmeme.module.member.form.SettingsPasswordForm;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.context.MessageSource;
import org.springframework.data.repository.query.Param;
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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController extends BaseController {

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final ApplicationProperties applicationProperties;
    private final TemplateEngine templateEngine;
    private final MessageSource messageSource;
    private final MemberService memberService;

    @GetMapping("/login/forgot")
    public String forgetPasswordForm(Model model) {
        setBaseAttributes(null, model, "forgot");
        model.addAttribute(new ForgotPasswordForm());

        return "forgotPassword";
    }

    @PostMapping("/login/forgot")
    public String forgetPassword(Model model,
                                 @Valid ForgotPasswordForm form, BindingResult result, HttpServletRequest request) {
        setBaseAttributes(null, model, "forgot");

        if(result.hasErrors()) {
            return "forgotPassword";
        }

        Member foundMember = memberRepository.findMemberByEmail(form.getEmail());
        if(foundMember == null) {
            result.rejectValue("email", "login.no-such-member", new Object[]{}, "?????? ???????????? ????????? ???????????? ????????????.");

            return "forgotPassword";
        }

        EmailMessage emailMessage = createForgotEmailMessage(request, foundMember);
        emailService.send(emailMessage);

        return "mail/emailSent";
    }

    @GetMapping("/login/forgot/check")
    public String checkPasswordForgot(@RequestParam String token, @RequestParam String email, Model model) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(
                email + "??? ???????????? member??? ????????????."
        ));

        if(!member.getEmailCheckToken().equals(token)) {
            return "error";
        }

        memberService.login(member);

        model.addAttribute(new SettingsPasswordForm());
        setBaseAttributes(member, model, "password");

        return "redirect:/settings/password";
    }

    private EmailMessage createForgotEmailMessage(HttpServletRequest request, Member foundMember) {
        // send email;
        Context context = new Context();

        context.setVariable("link", "?token=" + foundMember.getEmailCheckToken() + "&email=" + foundMember.getEmail());
        context.setVariable("host", applicationProperties.getHost() + request.getContextPath());
        context.setLocale(request.getLocale());

        String message = templateEngine.process("mail/sentPasswordForgotEmail", context);
        EmailMessage emailMessage = new EmailMessage(foundMember.getEmail(), messageSource.getMessage("email.forgot.subject",
                null, "????????? ???????????? ????????? ???????????????.", request.getLocale()), message);
        return emailMessage;
    }
}
