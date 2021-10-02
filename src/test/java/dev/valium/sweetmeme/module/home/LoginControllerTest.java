package dev.valium.sweetmeme.module.home;

import dev.valium.sweetmeme.module.info.Info;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberRepository;
import dev.valium.sweetmeme.module.member.MemberService;
import dev.valium.sweetmeme.module.member.SignUpService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;

    private static final String nickname = "membersdf1";
    private static final String email = "email2@email2.com";
    private static final String password = "membermember";

    @BeforeEach
    void beforeEach() {
        Member member = Member.createMember(nickname, email, password);
        Info info = Info.createInfo(null, "head", "description");

        member.setMemberInfo(info);

        memberService.saveMember(member);
    }

    @AfterEach
    void afterEach() {
        Member memberByNickname = memberRepository.findMemberByNickname(nickname);
        memberRepository.delete(memberByNickname);
    }


    @DisplayName("로그인 폼 화면이 보이는지")
    @Test
    public void signUpForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("로그인 성공")
    public void login() throws Exception {

        mockMvc.perform(post("/login")
                .param("username", email)
                .param("password", password)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(unauthenticated());
                //.andExpect(authenticated().withUsername(nickname));
    }

    @Test
    @DisplayName("로그인 실패")
    public void 로그인_실패() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "failfails")
                .param("password", "failfailfail")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @Test
    public void 로그아웃() throws Exception {
        mockMvc.perform(post("/logout")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }

}