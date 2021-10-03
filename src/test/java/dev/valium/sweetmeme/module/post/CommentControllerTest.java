package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberFactory;
import dev.valium.sweetmeme.module.member.MemberRepository;
import dev.valium.sweetmeme.module.member.MemberService;
import dev.valium.sweetmeme.module.member_post.MemberPostRepository;
import dev.valium.sweetmeme.module.section.SectionFactory;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;

    @Autowired private UploadService uploadService;
    @Autowired private PostRepository postRepository;
    @Autowired private SectionFactory sectionFactory;
    @Autowired private MemberPostRepository memberPostRepository;
    @Autowired private CommentRepository commentRepository;

    private final String NICKNAME = "membersdf1";
    private final String EMAIL = "email2@email2.com";
    private final String PASSWORD = "membermember";

    @BeforeEach
    void beforeEach() throws Exception {
        sectionFactory.init();

        Member member = MemberFactory.create(NICKNAME, EMAIL, PASSWORD, "description");

        memberService.saveMember(member);
        memberService.login(member);

        InputStream imageStream = new FileInputStream("D:\\sweetmeme\\test\\images\\file.jpg");
        byte[] bytes = IOUtils.toByteArray(imageStream);
        imageStream.close();

        MockMultipartFile file = new MockMultipartFile("file", "file.jpg", "image/jpg", bytes);

        uploadService.uploadPost(member, "testTitle", "[{\"value\":\"\"}]", "[{\"value\":\"FUNNY\"}]", file);
    }

    @AfterEach
    void afterEach() {
        memberPostRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("코맨트_등록_성공")
    public void 코맨트_등록_성공() throws Exception {
        Post post = getPost();
        MockMultipartFile file = createMockMultipartFile("file", false, false);

        mockMvc.perform(multipart("/post/comment/" + post.getId())
                .file(file)
                .param("content", "test content")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + post.getId()))
                .andExpect(authenticated());
    }

    @Test
    @DisplayName("코맨트_등록_실패")
    public void 코맨트_등록_실패() throws Exception {
        Post post = getPost();

        MockMultipartFile file = new MockMultipartFile("file", null, null, (byte[]) null);

        // 빈 폼
        mockMvc.perform(multipart("/post/comment/" + post.getId())
                .file(file)
                .param("content", "")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + post.getId()))
                .andExpect(flash().attributeExists("blankCommentForm"))
                .andExpect(authenticated());

        MockMultipartFile file2 = createMockMultipartFile("file", true, false);

        // 10mb 이상의 파일
        mockMvc.perform(multipart("/post/comment/" + post.getId())
                .file(file2)
                .param("content", "test content")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + post.getId()))
                .andExpect(flash().attributeExists("overSizedFile"))
                .andExpect(authenticated());

        MockMultipartFile file3 = createMockMultipartFile("file", false, true);

        // 지원하지 않는 확장자
        mockMvc.perform(multipart("/post/comment/" + post.getId())
                .file(file3)
                .param("content", "test content")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + post.getId()))
                .andExpect(flash().attributeExists("wrongFileType"))
                .andExpect(authenticated());
    }

    @Test
    @DisplayName("reply_등록_성공")
    public void reply_등록_성공() throws Exception {

        Post post = getPost();
        MockMultipartFile file = createMockMultipartFile("file", false, false);

        mockMvc.perform(multipart("/post/comment/" + post.getId())
                .file(file)
                .param("content", "test content")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + post.getId()))
                .andExpect(authenticated());

        Comment comment = commentRepository.findAll().get(0);

        mockMvc.perform(multipart("/reply/" + post.getId() + "/" + comment.getId())
                .file(file)
                .param("content", "test content")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + post.getId()))
                .andExpect(authenticated());
    }

    // comment와 reply는 사실상 url만 같기에 reply 성공만 테스트 함
    // public void reply_등록_실패() {}

    @Test
    @DisplayName("comment/reply 업보트 클릭 성공")
    public void comment_or_reply_upvote() throws Exception {

        Post post = getPost();
        MockMultipartFile file = createMockMultipartFile("file", false, false);

        mockMvc.perform(multipart("/post/comment/" + post.getId())
                .file(file)
                .param("content", "test content")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + post.getId()))
                .andExpect(authenticated());

        Comment comment = commentRepository.findAll().get(0);

        int upVote = comment.getVote().getUpVote();

        mockMvc.perform(post("/comment/" + comment.getId() + "/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(
                        "{" +
                                "\"id\" : \"" + comment.getId() + "\", " +
                                "\"vote\" : \"true\"" +
                                "}")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        Comment updatedComment = commentRepository.findAll().get(0);

        Assertions.assertEquals(upVote + 1, updatedComment.getVote().getUpVote());
    }

    @Test
    @DisplayName("comment/reply 다운보트 클릭 성공")
    public void comment_or_reply_downvote() throws Exception {

        Post post = getPost();
        MockMultipartFile file = createMockMultipartFile("file", false, false);

        mockMvc.perform(multipart("/post/comment/" + post.getId())
                .file(file)
                .param("content", "test content")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + post.getId()))
                .andExpect(authenticated());

        Comment comment = commentRepository.findAll().get(0);

        int downVote = comment.getVote().getDownVote();

        mockMvc.perform(post("/comment/" + comment.getId() + "/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(
                        "{" +
                                "\"id\" : \"" + comment.getId() + "\", " +
                                "\"vote\" : \"false\"" +
                                "}")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        Comment updatedComment = commentRepository.findAll().get(0);

        Assertions.assertEquals(downVote + 1, updatedComment.getVote().getDownVote());
    }

    private MockMultipartFile createMockMultipartFile(String name, boolean isOver10mb, boolean isWrongFileType) throws IOException {
        InputStream imageStream;
        byte[] bytes;
        MockMultipartFile mockMultipartFile;

        if(isOver10mb && !isWrongFileType) {
            imageStream = new FileInputStream("D:\\sweetmeme\\test\\images\\10mb.jpg");
            bytes = IOUtils.toByteArray(imageStream);
            imageStream.close();

            mockMultipartFile = new MockMultipartFile(name, "file.jpg", "image/jpg", bytes);
        }
        else if(!isOver10mb && isWrongFileType) {
            imageStream = new FileInputStream("D:\\sweetmeme\\test\\images\\icon.ico");
            bytes = IOUtils.toByteArray(imageStream);
            imageStream.close();

            mockMultipartFile = new MockMultipartFile(name, "file.ico", "image/icon", bytes);
        }
        else if(isOver10mb && isWrongFileType) {
            imageStream = new FileInputStream("D:\\sweetmeme\\test\\images\\10mb.jpg");
            bytes = IOUtils.toByteArray(imageStream);
            imageStream.close();

            mockMultipartFile = new MockMultipartFile(name, "file.ico", "image/icon", bytes);
        }
        else {
            imageStream = new FileInputStream("D:\\sweetmeme\\test\\images\\file.jpg");
            bytes = IOUtils.toByteArray(imageStream);
            imageStream.close();
            mockMultipartFile = new MockMultipartFile(name, "file.jpg", "image/jpg", bytes);
        }

        return mockMultipartFile;
    }
    private Post getPost() {
        Member member = memberRepository.findAll().get(0);
        Post post = postRepository.findAllByOriginalPosterOrderByCreatedDateDesc(member).get(0);
        return post;
    }

}