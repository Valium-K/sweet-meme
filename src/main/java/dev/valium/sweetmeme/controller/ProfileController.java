package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.domain.CurrentMember;
import dev.valium.sweetmeme.domain.Info;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/{path}")
public class ProfileController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping()
    public String profile(@PathVariable String path, Model model) {

        Member foundMember = memberService.getMember(path);
        model.addAttribute("spendDate", foundMember.getSpendDate());
        model.addAttribute("member", foundMember);


        // info, member,
        return "user/profile";
    }
}
