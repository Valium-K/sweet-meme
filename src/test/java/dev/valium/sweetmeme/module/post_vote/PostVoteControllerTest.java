package dev.valium.sweetmeme.module.post_vote;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberFactory;
import dev.valium.sweetmeme.module.member.MemberRepository;
import dev.valium.sweetmeme.module.member.MemberService;
import dev.valium.sweetmeme.module.member_post.MemberPostRepository;
import dev.valium.sweetmeme.module.post.Post;
import dev.valium.sweetmeme.module.post.PostRepository;
import dev.valium.sweetmeme.module.post.UploadService;
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
import java.io.InputStream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostVoteControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;

    @Autowired private UploadService uploadService;
    @Autowired private PostRepository postRepository;
    @Autowired private SectionFactory sectionFactory;
    @Autowired private MemberPostRepository memberPostRepository;

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
    @DisplayName("post 업보트 클릭 성공")
    public void post_upvote() throws Exception {
        Post post = getPost();
        String id = String.valueOf(post.getId());
        int upVote = post.getVote().getUpVote();

        mockMvc.perform(post("/post/" + post.getId() + "/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(
                        "{" +
                                "\"id\" : \"" + id + "\", " +
                                "\"vote\" : \"true\"" +
                                "}")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        Post updatedPost = getPost();

        Assertions.assertEquals(upVote + 1, updatedPost.getVote().getUpVote());
    }

    @Test
    @DisplayName("post 업보트 두번 클릭 성공")
    public void post_upvoteDouble() throws Exception {
        Post post = getPost();
        String id = String.valueOf(post.getId());
        int upVote = post.getVote().getUpVote();

        mockMvc.perform(post("/post/" + post.getId() + "/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(
                        "{" +
                                "\"id\" : \"" + id + "\", " +
                                "\"vote\" : \"true\"" +
                                "}")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(post("/post/" + post.getId() + "/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(
                        "{" +
                                "\"id\" : \"" + id + "\", " +
                                "\"vote\" : \"true\"" +
                                "}")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        Post updatedPost = getPost();

        Assertions.assertEquals(upVote, updatedPost.getVote().getUpVote());
    }

    @Test
    @DisplayName("post 다운보트 클릭 성공")
    public void post_downvote() throws Exception {
        Post post = getPost();
        String id = String.valueOf(post.getId());
        int downVote = post.getVote().getDownVote();

        mockMvc.perform(post("/post/" + post.getId() + "/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(
                        "{" +
                                "\"id\" : \"" + id + "\", " +
                                "\"vote\" : \"false\"" +
                                "}")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        Post updatedPost = getPost();

        Assertions.assertEquals(downVote + 1, updatedPost.getVote().getDownVote());
    }

    @Test
    @DisplayName("post 다운보트 두번 클릭 성공")
    public void post_downvoteDouble() throws Exception {
        Post post = getPost();
        String id = String.valueOf(post.getId());
        int downVote = post.getVote().getDownVote();

        mockMvc.perform(post("/post/" + post.getId() + "/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(
                        "{" +
                                "\"id\" : \"" + id + "\", " +
                                "\"vote\" : \"false\"" +
                                "}")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(post("/post/" + post.getId() + "/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(
                        "{" +
                                "\"id\" : \"" + id + "\", " +
                                "\"vote\" : \"false\"" +
                                "}")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        Post updatedPost = getPost();

        Assertions.assertEquals(downVote, updatedPost.getVote().getDownVote());
    }
    private Post getPost() {
        Member member = memberRepository.findAll().get(0);
        Post post = postRepository.findAllByOriginalPosterOrderByCreatedDateDesc(member).get(0);
        return post;
    }
}