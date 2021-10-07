package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.module.bases.BaseController;
import dev.valium.sweetmeme.module.comment_vote.CommentVoteService;
import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberService;
import dev.valium.sweetmeme.module.post.form.CommentForm;
import dev.valium.sweetmeme.module.post_vote.PostVoteService;
import dev.valium.sweetmeme.module.tag.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static dev.valium.sweetmeme.infra.config.FileConfig.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController extends BaseController {

    private final PostService postService;
    private final CommentRepository commentRepository;
    private final CommentVoteService commentVoteService;
    private final MemberService memberService;

    @GetMapping(FILE_URL + "{file}")
    public ResponseEntity<Resource> getPostFile(@PathVariable String file) throws IOException {

        InputStream imageStream = new FileInputStream(ABSOLUTE_UPLOAD_PATH + file);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity(imageByteArray, HttpStatus.OK);
    }

    @GetMapping(DOWNLOAD_URL + "{file}")
    public ResponseEntity<Resource> downloadPostFile(@PathVariable String file) {
        String path = ABSOLUTE_UPLOAD_PATH + file;

        try {
            Path filePath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

            File newFile = new File(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(newFile.getName()).build());

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/post/{id}")
    public String viewClickedPost(@PathVariable Long id, Model model, @CurrentMember Member member) {

        Post post = postService.findPostById(id);

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "vote.upVote", "createdDate"));

        Slice<Comment> comments = commentRepository.findByPostIdAndParent(post.getId(), null, pageRequest);
        List<String> tags = postService.findTags(post)
                            .stream()
                            .map(Tag::getTagName).collect(Collectors.toList());


        model.addAttribute(post);
        model.addAttribute("tags", tags);
        model.addAttribute("comments", comments);
        model.addAttribute(new CommentForm());

        setBaseAttributes(member, model, null);

        if(member != null) {
            List<Long> upVoteCommentIds = commentVoteService.findUpVotedCommentsId(member);
            member.setUpVoteCommentIds(upVoteCommentIds);
            List<Long> downVoteCommentIds = commentVoteService.findDownVotedCommentsId(member);
            member.setDownVoteCommentIds(downVoteCommentIds);

            memberService.updatePrincipal(member);
        }

        if(comments.isLast()) {
            model.addAttribute("itIsLastCommentPage", true);
        }

        return "post/clickedPost";
    }
}
