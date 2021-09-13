package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.domain.CurrentMember;
import dev.valium.sweetmeme.domain.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/postProcess")
    public String postProcess(@CurrentMember Member member) {
        return "redirect:/";
    }
}
