package dev.valium.sweetmeme.service;

import dev.valium.sweetmeme.controller.dto.SignUpForm;
import dev.valium.sweetmeme.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final PasswordEncoder passwordEncoder;

    public Member form2Member(SignUpForm form) {
        String encodedPassword = passwordEncoder.encode(form.getPassword());

        return Member.createMember(form.getNickname(), form.getEmail(), encodedPassword);
    }
}
