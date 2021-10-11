package dev.valium.sweetmeme.module.member;

import dev.valium.sweetmeme.infra.config.FileConfig;
import dev.valium.sweetmeme.module.member.form.SettingsAccountForm;
import dev.valium.sweetmeme.module.member.form.SettingsPasswordForm;
import dev.valium.sweetmeme.module.member.form.SettingsProfileForm;
import dev.valium.sweetmeme.module.member.validator.SettingsProfileValidator;
import dev.valium.sweetmeme.module.section.enums.SectionType;
import dev.valium.sweetmeme.module.processor.Code2State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final MemberService memberService;
    private final Code2State code2State = new Code2State();
    private final MemberRepository memberRepository;
    private final SettingsProfileValidator settingsProfileValidator;

    @InitBinder("settingsProfileForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(settingsProfileValidator);
    }

    @GetMapping("/settings/account")
    public String settingsAccountForm(@CurrentMember Member member, Model model) {
        setBasicAttribute(member, model, "account");
        model.addAttribute(new SettingsAccountForm(member));

        return "user/settings/account";
    }

    @PostMapping("/settings/account")
    public String settingsAccount(@CurrentMember Member member, Model model, RedirectAttributes attributes,
                                  @Valid SettingsAccountForm form, BindingResult result) {
        if(result.hasErrors()) {

            setBasicAttribute(member, model, "account");

            return "user/settings/account";
        }

        if(!member.getNickname().equals(form.getNickname()) &&
                memberRepository.findMemberByNickname(form.getNickname()) != null) {
            result.rejectValue("nickname", "duplicated.nickname", new Object[]{form.getNickname()},
                    "이미 사용중인 닉네임입니다.");

            setBasicAttribute(member, model, "account");

            return "user/settings/account";
        }

        Member changedMember = memberService.updateMemberAccount(member, form);

        attributes.addFlashAttribute("accountChanged", "변경사항을 적용하였습니다.");

        return "redirect:/settings/account";
    }

    @PostMapping("/settings/account/delete")
    public String deleteAccount(@CurrentMember Member member, HttpServletRequest request, HttpServletResponse response) {
        log.info("로그아웃");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        memberService.deleteAccount(member);

        return "redirect:/";
    }

    @GetMapping("/settings/profile")
    public String settingsProfileForm(@CurrentMember Member member, Model model) throws Exception {
        String state = code2State.findStateByCode(member.getMemberInfo().getStateCode());

        model.addAttribute(new SettingsProfileForm(member, state));
        setBasicAttribute(member, model, "profile");

        model.addAttribute("ACCEPTABLE_AVATAR_TYPES", FileConfig.ACCEPTABLE_AVATAR_TYPES);

        return "user/settings/profile";
    }

    @PostMapping("/settings/profile")
    public String settingsProfile(@CurrentMember Member member, Model model, RedirectAttributes attributes,
                                  @Valid SettingsProfileForm form, BindingResult result) throws IOException {

        if(result.hasErrors()) {
            setBasicAttribute(member, model, "profile");
            model.addAttribute("ACCEPTABLE_AVATAR_TYPES", FileConfig.ACCEPTABLE_AVATAR_TYPES);

            return "user/settings/profile";
        }

        memberService.updateProfile(member, form);

        attributes.addFlashAttribute("profileChanged", "프로필을 변경하였습니다.");

        return "redirect:/settings/profile";
    }

    @PostMapping("/settings/profile/reset/avatar")
    @ResponseBody
    public ResponseEntity resetAvatar(@CurrentMember Member member) {
        memberService.resetProfileAvatar(member);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/settings/password")
    public String settingsPasswordForm(@CurrentMember Member member, Model model) {
        model.addAttribute(new SettingsPasswordForm());
        setBasicAttribute(member, model, "password");

        return "user/settings/password";
    }

    @PostMapping("/settings/password")
    public String settingPassword(@CurrentMember Member member, Model model, RedirectAttributes attributes,
                                  @Valid SettingsPasswordForm form, BindingResult result) {
        if(result.hasErrors()) {
            setBasicAttribute(member, model, "password");

            return "user/settings/password";
        }

        if(!form.getPassword().equals(form.getPasswordConfirm())) {
            setBasicAttribute(member, model, "password");
            result.rejectValue("passwordConfirm", "common.passwordConfirm.error",
                    new Object[]{form.getPassword()}, "비밀번호와 비밀번호 확인이 일치하지 않습니다.");

            return "user/settings/password";
        }

        memberService.updatePassword(member, form.getPassword());

        attributes.addFlashAttribute("passwordChanged", "비밀번호를 변경하였습니다.");

        return "redirect:/settings/password";
    }

    private void setBasicAttribute(Member member, Model model, String settingsTab) {
        model.addAttribute("settingsMenu", settingsTab);
        model.addAttribute("member", member);
        model.addAttribute("info", member.getMemberInfo());
        model.addAttribute("spendDate", member.getSpendDate());

        List<String> sectionTypes = Arrays.stream(SectionType.values()).map(s->s.name().toLowerCase()).collect(Collectors.toList());
        model.addAttribute("sidebarSectionTypes", sectionTypes);
    }

}
