package dev.valium.sweetmeme.module.member;

import dev.valium.sweetmeme.module.bases.enums.SectionType;
import dev.valium.sweetmeme.module.info.Info;
import dev.valium.sweetmeme.module.member.form.SignUpForm;
import dev.valium.sweetmeme.module.member.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final MemberService memberService;
    private final SignUpService signUpService;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }


    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());

        List<String> sectionTypes = Arrays.stream(SectionType.values()).map(s->s.name().toLowerCase()).collect(Collectors.toList());
        model.addAttribute("sidebarSectionTypes", sectionTypes);

        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpFormCreate(@Valid SignUpForm signUpForm, BindingResult result, Model model) {

        if(result.hasErrors()) {
            return "sign-up";
        }

        Member member = signUpService.form2Member(signUpForm);
        Info info = Info.createInfo(null, member.getNickname(), member.getNickname() + " description");

        // TODO 적당한 이름 생각나면 메서드로 빼기
        member.setMemberInfo(info);
        info.setHead(member.getNickname());

        Member savedMember = memberService.saveMember(member);

        memberService.login(savedMember);

        return "redirect:/";
    }

}
