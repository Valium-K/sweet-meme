package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
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
    private String password = "member";

    @DisplayName("회원가입 화면이 보이는지")
    @Test
    public void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @Test
    @DisplayName("회원 가입 처리 - 입력값 오류")
    public void 회원가입처리_입력값오류() throws Exception {

        String wrongEmail = "worng.email";
        String wrongNickname = "ln";
        String wrongPassword = "pw";

        mockMvc.perform(post("/sign-up")
                .param("nickname", wrongNickname)
                .param("email", email)
                .param("password", password)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("member/sign-up"))
                .andExpect(unauthenticated());

        mockMvc.perform(post("/sign-up")
                .param("nickname", email)
                .param("email", wrongEmail)
                .param("password", password)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("member/sign-up"))
                .andExpect(unauthenticated());

        mockMvc.perform(post("/sign-up")
                .param("nickname", email)
                .param("email", email)
                .param("password", wrongPassword)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("member/sign-up"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("회원 가입 처리 - 입력값 정상")
    public void 회원가입처리_입력값정상() throws Exception {

        mockMvc.perform(post("/sign-up")
                .param("nickname", nickname)
                .param("email", email)
                .param("password", password)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername(nickname));

        assertNotNull(memberRepository.findByEmail(email));
    }

}