package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.module.bases.BaseController;
import dev.valium.sweetmeme.module.comment.Comment;
import dev.valium.sweetmeme.module.comment.CommentRepository;
import dev.valium.sweetmeme.module.comment.form.CommentForm;
import dev.valium.sweetmeme.module.comment_vote.CommentVoteService;
import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post.form.UploadForm;
import dev.valium.sweetmeme.module.tag.Tag;
import dev.valium.sweetmeme.module.post_vote.PostVoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.valium.sweetmeme.infra.config.FileConfig.*;

@Slf4j
@Controller
public class PostController extends BaseController {

    private final PostService postService;
    private final CommentRepository commentRepository;
    private final CommentVoteService commentVoteService;

    public PostController(PostVoteService postVoteService, PostService postService, CommentRepository commentRepository, CommentVoteService commentVoteService) {
        super(postVoteService);
        this.postService = postService;
        this.commentRepository = commentRepository;
        this.commentVoteService = commentVoteService;
    }

    @GetMapping(FILE_URL + "{file}")
    public ResponseEntity<Resource> getFile(@PathVariable String file) throws IOException {

        InputStream imageStream = new FileInputStream(ABSOLUTE_UPLOAD_PATH + file);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity(imageByteArray, HttpStatus.OK);
    }

    @GetMapping(DOWNLOAD_URL + "{file}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String file) {
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

    @GetMapping(SECTION_URL + "{sectionName}")
    public ResponseEntity<byte[]> getSectionPic(@PathVariable String sectionName) throws IOException {

        InputStream imageStream = new FileInputStream(ABSOLUTE_SECTION_PATH + sectionName.toLowerCase() + ".jpg");
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    public String viewClickedPost(@PathVariable Long id, Model model, @CurrentMember Member member) {

        Post post = postService.findPostById(id);

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "vote.upVote", "createdDate"));

        Slice<Comment> comments = commentRepository.findByPostAndParent(post, null, pageRequest);
        List<String> tags = postService.findTags(post)
                            .stream()
                            .map(Tag::getTagName).collect(Collectors.toList());


        model.addAttribute(post);
        model.addAttribute("tags", tags);
        model.addAttribute("comments", comments);

        model.addAttribute(new CommentForm());

        setBaseAttributes(member, model, null);

        List<Long> upVoteCommentIds = commentVoteService.findUpVotedCommentsId(member);
        model.addAttribute("upVoteCommentIds", upVoteCommentIds);
        List<Long> downVoteCommentIds = commentVoteService.findDownVotedCommentsId(member);
        model.addAttribute("downVoteCommentIds", downVoteCommentIds);

        if(comments.isLast()) {
            model.addAttribute("itIsLastCommentPage", true);
        }

        return "post/clickedPost";
    }

    @PostMapping("/post/{id}")
    public String postComment(@PathVariable Long id, Model model, @CurrentMember Member member,
                              @Valid CommentForm form, BindingResult result) throws IOException {

        if(form.getFile().isEmpty() && form.getContent().isEmpty()) {
//            model.addAttribute(member);
//            result.rejectValue("content", "comment.blankContentOrFile", new Object[]{},"코멘트나 파일을 작성");
            return "redirect:/post/" + id;
        }
        postService.saveComment(id, form, member);

        return "redirect:/post/" + id;
    }

    @PostMapping("/reply/{postId}/{parentCommentId}")
    public String postComment(@PathVariable Long postId, @PathVariable Long parentCommentId, Model model, @CurrentMember Member member,
                              @Valid CommentForm form, BindingResult result) throws IOException {

        if(form.getFile().isEmpty() && form.getContent().isEmpty()) {
//            model.addAttribute(member);
//            result.rejectValue("content", "comment.blankContentOrFile", new Object[]{},"코멘트나 파일을 작성");
            return "redirect:/post/" + postId;
        }
        postService.saveReply(postId, parentCommentId, form, member);

        return "redirect:/post/" + postId;
    }

    @PostMapping("/comment/{id}/vote")
    @ResponseBody
    public ResponseEntity votePost(@CurrentMember Member member, @RequestBody Map<String, String> params) throws Exception {
        Long id = Long.valueOf(params.get("id"));
        boolean vote = Boolean.parseBoolean(params.get("vote"));

        boolean success = commentVoteService.voteComment(member, id, vote);
        if(success)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    @GetMapping("/reply/slice/" + "{commentId}" + "/" + "{page}")
    public String viewReply(@CurrentMember Member member, Model model, @PathVariable Long commentId, @PathVariable int page) {

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "vote.upVote", "createdDate"));

        Comment parent = commentRepository.findCommentById(commentId);
        Slice<Comment> replys = commentRepository.findByParent(parent, pageRequest);

        // TODO post, upVoteCommentIds, downVoteCommentIds 추가 쿼리
        List<Long> upVoteCommentIds = commentVoteService.findUpVotedCommentsId(member);
        model.addAttribute("upVoteCommentIds", upVoteCommentIds);
        List<Long> downVoteCommentIds = commentVoteService.findDownVotedCommentsId(member);
        model.addAttribute("downVoteCommentIds", downVoteCommentIds);
        model.addAttribute("post", parent.getPost());
        model.addAttribute("comments", replys);
        model.addAttribute(new CommentForm());

        model.addAttribute("commentId", commentId);
        model.addAttribute("itIsLastCommentPage", true);
        model.addAttribute("target", "reply");

        if(replys.isLast()) {
            model.addAttribute("itIsLastReplyPage", true);
        }


        return "fragments :: comments";
    }

    @GetMapping("/comment/slice/" + "{postId}" + "/" + "{page}")
    public String viewComment(@CurrentMember Member member, Model model, @PathVariable Long postId, @PathVariable int page) {
        Post post = postService.findPostById(postId);
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "vote.upVote", "createdDate"));
        Slice<Comment> comments = commentRepository.findByPostAndParent(post, null, pageRequest);

        // TODO post, upVoteCommentIds, downVoteCommentIds 추가 쿼리
        List<Long> upVoteCommentIds = commentVoteService.findUpVotedCommentsId(member);
        model.addAttribute("upVoteCommentIds", upVoteCommentIds);
        List<Long> downVoteCommentIds = commentVoteService.findDownVotedCommentsId(member);
        model.addAttribute("downVoteCommentIds", downVoteCommentIds);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute(new CommentForm());

        model.addAttribute("target", "comment");
        if(comments.isLast()) {
            log.info("last");
            model.addAttribute("itIsLastCommentPage", true);
        }

        return "fragments :: comments";
    }
}
