package dev.valium.sweetmeme.controller.validator;

import dev.valium.sweetmeme.controller.dto.SignUpForm;
import dev.valium.sweetmeme.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        SignUpForm signUpForm = (SignUpForm) target;

        if(memberRepository.findMemberByEmail(signUpForm.getEmail()) != null) {
            errors.rejectValue("email", "duplicated.email", new Object[]{signUpForm.getEmail()},
                    "이미 사용중인 이메일입니다.");
        }

        if(memberRepository.findMemberByNickname(signUpForm.getNickname()) != null) {
            errors.rejectValue("nickname", "duplicated.nickname", new Object[]{signUpForm.getEmail()},
                    "이미 사용중인 닉네임입니다.");
        }

        if(!signUpForm.getPassword().equals(signUpForm.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "common.passwordConfirm.error", new Object[]{signUpForm.getEmail()},
                    "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
    }
}
