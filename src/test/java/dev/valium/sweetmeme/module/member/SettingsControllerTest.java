package dev.valium.sweetmeme.module.member;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SettingsControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;

    private final String nickname = "membersdf1";
    private final String email = "email2@email2.com";
    private final String password = "membermember";

    private final String CHANGE_NICKNAME = "testName2";
    @BeforeEach
    void beforeEach() {
        Member member = MemberFactory.create(nickname, email, password, "description");

        memberService.saveMember(member);
        memberService.login(member);
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("설정화면이_잘_보이는가")
    public void 설정화면이_잘_보이는가() throws Exception {
        mockMvc.perform(get("/settings/account"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/settings/account"));

        mockMvc.perform(get("/settings/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/settings/profile"));

        mockMvc.perform(get("/settings/password"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/settings/password"));
    }

    @Test @DisplayName("닉네임_수정_정상")
    public void 닉네임_수정_정상() throws Exception {
        mockMvc.perform(post("/settings/account")
                        .param("nickname", CHANGE_NICKNAME)
                        .param("upvoteAlert", "true")
                        .param("replyAlert", "true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/account"))
                .andExpect(flash().attributeExists("accountChanged"))
                .andExpect(authenticated());
    }

    @Test @DisplayName("닉네임_수정_실패")
    public void 닉네임_수정_실패() throws Exception {
        mockMvc.perform(post("/settings/account")
                .param("nickname", "nickname with * $ @")
                .param("upvoteAlert", "true")
                .param("replyAlert", "true")
                .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(authenticated());

        mockMvc.perform(post("/settings/account")
                .param("nickname", "sooooooooooooooooooooooooooooooooooooooooooooooooooooooooo_loooooooooooooooooooooooooooooooooooooong_nicknameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")
                .param("upvoteAlert", "true")
                .param("replyAlert", "true")
                .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(authenticated());

        mockMvc.perform(post("/settings/account")
                .param("nickname", CHANGE_NICKNAME)
                .param("upvoteAlert", "not bool value")
                .param("replyAlert", "test")
                .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(authenticated());
    }

    @Test @DisplayName("알림변경")
    public void 알림변경() throws Exception {
        mockMvc.perform(post("/settings/account")
                .param("nickname", CHANGE_NICKNAME)
                .param("upvoteAlert", "true")
                .param("replyAlert", "false")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/account"))
                .andExpect(flash().attributeExists("accountChanged"))
                .andExpect(authenticated());
    }

    @Test @DisplayName("프로필_변경_정상")
    public void 프로필_변경_정상() throws Exception {

        String flagJson = "[{\"value\":\"Faroe Islands\",\"code\":\"FO\"}]";

        InputStream imageStream = new FileInputStream("D:\\sweetmeme\\test\\images\\file.jpg");
        byte[] bytes = IOUtils.toByteArray(imageStream);
        imageStream.close();

        MockMultipartFile file = new MockMultipartFile("file", "file.jpg", "image/jpg", bytes);

        mockMvc.perform(multipart("/settings/profile")
                        .file(file)
                        .param("description", "description")
                        .param("state",flagJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("profileChanged"))
                .andExpect(authenticated());
    }

    @Test @DisplayName("아바타_변경_실패")
    public void 아바타_변경_실패() throws Exception {

        String flagJson = "[{\"value\":\"Faroe Islands\",\"code\":\"FO\"}]";

        InputStream imageStream = new FileInputStream("D:\\sweetmeme\\test\\images\\file.jpg");
        byte[] bytes = IOUtils.toByteArray(imageStream);
        imageStream.close();

        MockMultipartFile file = new MockMultipartFile("file", "file.jpg", "image/icon", bytes);

        mockMvc.perform(multipart("/settings/profile")
                .file(file)
                .param("description", "description")
                .param("state",flagJson)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(authenticated());

        InputStream imageStream2 = new FileInputStream("D:\\sweetmeme\\test\\images\\10mb.jpg");
        byte[] bytes2 = IOUtils.toByteArray(imageStream2);
        imageStream2.close();

        MockMultipartFile file2 = new MockMultipartFile("file", "10mb.jpg", "image/jpg", bytes2);

        mockMvc.perform(multipart("/settings/profile")
                .file(file2)
                .param("description", "description")
                .param("state",flagJson)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(authenticated());
    }

    @Test @DisplayName("description_변경_성공")
    public void description_변경_성공() throws Exception {

    }

    @Test @DisplayName("description_변경_실패")
    public void description_변경_실패() throws Exception {

    }

    @Test @DisplayName("국기_변경_성공")
    public void 국기_변경_성공() throws Exception {

    }

    @Test @DisplayName("국기_변경_실패")
    public void 국기_변경_실패() throws Exception {

    }

    @Test @DisplayName("패스워드_변경_성공")
    public void 패스워드_변경_성공() throws Exception {

    }
    @Test @DisplayName("패스워드_변경_실패")
    public void 패스워드_변경_실패() throws Exception {

    }


}