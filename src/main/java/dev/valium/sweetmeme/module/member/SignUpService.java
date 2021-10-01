package dev.valium.sweetmeme.module.member;

import dev.valium.sweetmeme.module.member.form.SignUpForm;
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
