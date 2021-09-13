package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.domain.CurrentMember;
import dev.valium.sweetmeme.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings/{path}")
public class SettingsController {

//    @GetMapping("/account")
//    public String settingsAccountForm(@CurrentMember Member member, @PathVariable String path, Model model) {
//
//        member.
//
//        model
//    }
}
