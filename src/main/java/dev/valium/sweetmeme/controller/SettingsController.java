package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.config.FileConfig;
import dev.valium.sweetmeme.controller.dto.SettingsAccountForm;
import dev.valium.sweetmeme.controller.dto.SettingsProfileForm;
import dev.valium.sweetmeme.converter.Code2State;
import dev.valium.sweetmeme.domain.CurrentMember;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.service.MemberFetchService;
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

    private final MemberFetchService memberFetchService;
    private final Code2State code2State;

    @GetMapping("/account")
    public String settingsAccountForm(@CurrentMember Member member, @PathVariable String path, Model model) {
        setBasicAttribute(member, model);
        model.addAttribute("settingsMenu", "account");
        model.addAttribute(new SettingsAccountForm());

        return "user/settings/account";
    }

    @GetMapping("/profile")
    public String settingsProfileForm(@CurrentMember Member member, @PathVariable String path, Model model) throws Exception {
        String state = code2State.findStateByCode(member.getMemberInfo().getStateCode());
        model.addAttribute(new SettingsProfileForm(member, state));
        setBasicAttribute(member, model);
        model.addAttribute("settingsMenu", "profile");
        model.addAttribute("ACCEPTABLE_AVATAR_TYPES", FileConfig.ACCEPTABLE_AVATAR_TYPES);

        return "user/settings/profile";
    }

    private void setBasicAttribute(Member member, Model model) {
        model.addAttribute(member);
        model.addAttribute("info", member.getMemberInfo());
        model.addAttribute("spendDate", member.getSpendDate());
    }
}
