package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.controller.dto.SignUpForm;
import dev.valium.sweetmeme.domain.Info;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.service.MemberService;
import dev.valium.sweetmeme.service.SignUpService;
import org.json.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class UploadControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;
    @Autowired private SignUpService signUpService;

    private String nickname = "membersdf1";
    private String email = "email2@email2.com";
    private String password = "membermember";

    @BeforeEach void beforeEach() {

        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname(nickname);
        signUpForm.setEmail(email);
        signUpForm.setPassword(password);

        Member member = signUpService.form2Member(signUpForm);
        Info info = Info.createInfo(null, member.getNickname(), member.getNickname() + " description");

        member.setMemberInfo(info);
        info.setHead(member.getNickname());

        Member savedMember = memberService.saveMember(member);

        memberService.login(savedMember);

    }
    @AfterEach void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("업로드_화면이_잘_보이는지")
    public void 업로드_화면이_잘_보이는지() throws Exception {
        mockMvc.perform(get("/upload"))
                .andExpect(status().isOk())
                .andExpect(view().name("upload"));
    }

    @Test @DisplayName("업로드처리_입력값_정상")
    public void 업로드처리_입력값_정상() throws Exception {
        String title = "this is test title";
        String sectionType = "[{\"value\":\"FUNNY\"}]";
        JSONArray objects = new JSONArray(sectionType);

        MockMultipartFile file = new MockMultipartFile("file", "file.jpg", "image/jpg", "test file".getBytes());

        mockMvc.perform(multipart("/upload")

                //.file(file)
                .param("title", title)
                .param("sections",sectionType)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("home"))
                .andExpect(authenticated());
    }

    @Test @DisplayName("업로드처리_입력값_오류")
    public void 업로드처리_입력값_오류() throws Exception {
        String wrongTitle_Short = "a";
        String wrongSectionType = "[{'value'='NOT_FUNNY}]";
        String wrongSection_null = "";
        String wrongFileType = "image/icon";

        String title = "this is test title";
        String SectionType = "[{'value'='FUNNY}]";

        MultipartFile multipartFile = new MockMultipartFile("testFile", new FileInputStream(new File("D:/sweetmeme/test/images/test.jpg")));


    }

}