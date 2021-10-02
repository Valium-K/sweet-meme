package dev.valium.sweetmeme.module.member;

import dev.valium.sweetmeme.module.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SignUpControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private MemberRepository memberRepository;


    private String nickname = "member1";
    private String email = "email@email.com";
    private String password = "membermember";
    private String passwordConfirm = "membermember";

    @DisplayName("회원가입 화면이 보이는지")
    @Test
    public void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @Test
    @DisplayName("회원 가입 처리 - 입력값 오류")
    public void 회원가입처리_입력값오류() throws Exception {

        String wrongEmail = "worng.email";
        String wrongNickname = "lnqwfo***i";
        String wrongPassword = "pwfweoiweno";
        String wrongPasswordConfirm = "pwfweoiweno12324";

        mockMvc.perform(post("/sign-up")
                .param("nickname", wrongNickname)
                .param("email", email)
                .param("password", password)
                .param("passwordConfirm", passwordConfirm)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(unauthenticated());

        mockMvc.perform(post("/sign-up")
                .param("nickname", email)
                .param("email", wrongEmail)
                .param("password", password)
                .param("passwordConfirm", passwordConfirm)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(unauthenticated());

        mockMvc.perform(post("/sign-up")
                .param("nickname", email)
                .param("email", email)
                .param("password", wrongPassword)
                .param("passwordConfirm", wrongPassword)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(unauthenticated());
        // TODO 짧은 길이의 패스워드

        // TODO 잘못된 형식의 닉네임

    }

    @Test
    @DisplayName("회원 가입 처리 - 비밀번호 != 확인")
    public void 회원가입처리_비밀번호확인다름() throws Exception {

        mockMvc.perform(post("/sign-up")
                .param("nickname", nickname)
                .param("email", email)
                .param("password", password)
                .param("passwordConfirm", "asdfwefwefwef")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(unauthenticated());
    }
    @Test
    @DisplayName("회원 가입 처리 - 입력값 정상")
    public void 회원가입처리_입력값정상() throws Exception {

        mockMvc.perform(post("/sign-up")
                .param("nickname", nickname)
                .param("email", email)
                .param("password", password)
                .param("passwordConfirm", passwordConfirm)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername(nickname));

        Member byEmail = memberRepository.findByEmail(email).get();
        assertNotNull(byEmail);

        memberRepository.delete(byEmail);
    }

}