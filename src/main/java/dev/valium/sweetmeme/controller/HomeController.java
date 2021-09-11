package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.domain.CurrentMember;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping
    public String home(@CurrentMember Member member, Model model) {
        if(member == null) {
            model.addAttribute("member", null);
        } else {
            Member mem = memberRepository.findFetchInfoById(member.getId()).orElse(null);

            model.addAttribute("member", mem);
        }

        return "home";

    }


}
