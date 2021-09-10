package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.controller.dto.SignUpForm;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.service.MemberService;
import dev.valium.sweetmeme.service.SignUpService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProfileControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;
    @Autowired private SignUpService signUpService;

    private String nickname = "membersdf1";
    private String email = "email2@email2.com";
    private String password = "membermember";

    @BeforeEach
    void beforeEach() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname(nickname);
        signUpForm.setEmail(email);
        signUpForm.setPassword(password);

        Member member = signUpService.form2Member(signUpForm);
        memberService.saveMember(member);
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("프로필_홈_화면이_보이는지")
    public void 프로필_홈_화면이_보이는지() throws Exception {
        mockMvc.perform(get("/user/" + nickname + "/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/home"));
    }

    @Test @DisplayName("프로필_포스트_화면이_보이는지")
    public void 프로필_포스트_화면이_보이는지() throws Exception {
        mockMvc.perform(get("/user/" + nickname + "/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/posts"));
    }

    @Test @DisplayName("프로필_코멘트_화면이_보이는지")
    public void 프로필_코멘트_화면이_보이는지() throws Exception {
        mockMvc.perform(get("/user/" + nickname + "/comments"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/comments"));
    }

    @Test @DisplayName("프로필_업보트_화면이_보이는지")
    public void 프로필_업보트_화면이_보이는지() throws Exception {
        mockMvc.perform(get("/user/" + nickname + "/upvotes"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/upvotes"));
    }


}