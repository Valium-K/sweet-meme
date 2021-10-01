package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.module.info.Info;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.section.Section;
import dev.valium.sweetmeme.module.bases.enums.SectionType;
import dev.valium.sweetmeme.module.member.MemberRepository;
import dev.valium.sweetmeme.module.member.MemberService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UploadControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;
    @Autowired private EntityManager entityManager;
    @Autowired private MessageSource messageSource;

    private String nickname = "membersdf1";
    private String email = "email2@email2.com";
    private String password = "membermember";

    @BeforeEach void beforeEach() {
        Member member = Member.createMember(nickname, email, password);
        Info info = Info.createInfo(null, "head", "description");

        member.setMemberInfo(info);

        memberService.saveMember(member);

        memberService.login(member);
    }
    @AfterEach void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("업로드_화면이_잘_보이는지")
    public void 업로드_화면이_잘_보이는지() throws Exception {
        mockMvc.perform(get("/upload/post"))
                .andExpect(status().isOk());

    }

    @Test @DisplayName("업로드처리_입력값_정상")
    public void 업로드처리_입력값_정상() throws Exception {
        sectionInit();

        String title = "this is test title";
        String sectionType = "[{\"value\":\"FUNNY\"}]";

        InputStream imageStream = new FileInputStream("D:\\sweetmeme\\test\\images\\file.jpg");
        byte[] bytes = IOUtils.toByteArray(imageStream);
        imageStream.close();

        MockMultipartFile file = new MockMultipartFile("file", "file.jpg", "image/jpg", bytes);

        mockMvc.perform(multipart("/upload/post")
                .file(file)
                .param("title", title)
                .param("sections",sectionType)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated());
    }

    @Test @DisplayName("업로드처리_입력값_오류 - 짧은 타이틀")
    public void 업로드처리_입력값_오류() throws Exception {
        String sectionType = "[{\"value\":\"FUNNY\"}]";
        MockMultipartFile file = new MockMultipartFile("file", "file.jpg", "image/jpg", "test file".getBytes());
        String wrongTitle_Short = "a";

        mockMvc.perform(multipart("/upload/post")
                .file(file)
                .param("title", wrongTitle_Short)
                .param("sections",sectionType)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("upload"))
                .andExpect(authenticated());
    }

    @Test @DisplayName("업로드처리_입력값_오류 - 정의되지 않은 섹션")
    public void 업로드처리_입력값_오류2() throws Exception {
        String title = "this is test title";
        MockMultipartFile file = new MockMultipartFile("file", "file.jpg", "image/jpg", "test file".getBytes());
        String wrongSectionType = "[{\"value\":\"NOT_FUNNY\"}]";

        mockMvc.perform(multipart("/upload/post")
                .file(file)
                .param("title", title)
                .param("sections",wrongSectionType)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("upload"))
                .andExpect(authenticated());
    }


    @Test @DisplayName("업로드처리_입력값_오류 - 정의되지 않은 확장자")
    public void 업로드처리_입력값_오류3() throws Exception {
        String title = "this is test title";
        String sectionType = "[{\"value\":\"FUNNY\"}]";
        MockMultipartFile wrongFile = new MockMultipartFile("file", "file.jpg", "image/icon", "test test".getBytes());

        mockMvc.perform(multipart("/upload/post")
                .file(wrongFile)
                .param("title", title)
                .param("sections",sectionType)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("upload"))
                .andExpect(authenticated());
    }

    private void sectionInit() {
        Arrays.asList(SectionType.values()).forEach(sectionType -> {
            Section section = Section.createSection(
                    sectionType
                    , Info.createInfo(
                            sectionType.name().toLowerCase(),
                            sectionType.name().toUpperCase(),
                            messageSource.getMessage("section." + sectionType.name().toLowerCase(Locale.US) + ".description"
                                    , new Object[0]
                                    , Locale.US)
                    ));
            entityManager.persist(section);
        });
    }
}