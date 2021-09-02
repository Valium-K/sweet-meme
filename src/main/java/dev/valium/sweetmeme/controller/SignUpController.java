package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.controller.dto.SignUpForm;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.service.MemberService;
import dev.valium.sweetmeme.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final SignUpService service;

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());

        return "member/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpFormCreate(@Valid SignUpForm signUpForm, BindingResult result) {

        if(result.hasErrors()) {
            return "member/sign-up";
        }

        if(memberRepository.findByEmail(signUpForm.getEmail()) != null) {
            result.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()},
                    "이미 사용중인 이메일입니다.");

            return "member/sign-up";
        }

        Member member = service.form2Member(signUpForm);
        Member savedMember = memberService.saveMember(member);

        memberService.login(savedMember);

        return "redirect:/";
    }

}
