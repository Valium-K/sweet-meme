package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.infra.config.FileConfig;
import dev.valium.sweetmeme.module.comment_vote.CommentVoteService;
import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberService;
import dev.valium.sweetmeme.module.post.form.CommentForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static dev.valium.sweetmeme.infra.config.FileConfig.ABSOLUTE_COMMENT_IMAGE_PATH;
import static dev.valium.sweetmeme.infra.config.FileConfig.COMMENT_IMAGE_URL;

/**
 * 코멘트와 코멘트 댓글관련 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final PostService postService;
    private final CommentVoteService commentVoteService;
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping(COMMENT_IMAGE_URL + "{file}")
    @ResponseBody
    public ResponseEntity<byte[]> getImageComment(@PathVariable String file) throws Exception {

        InputStream imageStream = new FileInputStream(ABSOLUTE_COMMENT_IMAGE_PATH + file);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    @PostMapping("/post/comment/{id}")
    public String postComment(@PathVariable Long id, @CurrentMember Member member,
                              CommentForm form, RedirectAttributes attributes) throws IOException {

        if(isThereErrorsInCommentForm(id, form, attributes)) return "redirect:/post/" + id;

        postService.saveComment(id, form, member);

        return "redirect:/post/" + id;
    }
    @PostMapping("/reply/{postId}/{parentCommentId}")
    public String postReply(@PathVariable Long postId, @PathVariable Long parentCommentId, Model model, @CurrentMember Member member,
                            CommentForm form, RedirectAttributes attributes) throws IOException {

        if(isThereErrorsInCommentForm(postId, form, attributes)) return "redirect:/post/" + postId;

        postService.saveReply(postId, parentCommentId, form, member);

        return "redirect:/post/" + postId;
    }

    @PostMapping("/comment/{id}/vote")
    @ResponseBody
    public ResponseEntity voteCommentOrReply(@CurrentMember Member member, @RequestBody Map<String, String> params) throws Exception {
        Long id = Long.valueOf(params.get("id"));
        boolean vote = Boolean.parseBoolean(params.get("vote"));

        Member updatedMember = commentVoteService.voteComment(member, id, vote);
        memberService.updatePrincipal(updatedMember);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/comment/{commentId}/{postId}/delete")
    public String deleteComment(@CurrentMember Member member, @PathVariable Long commentId,  @PathVariable Long postId, HttpServletRequest request) {

        commentService.deleteCommentById(member, commentId, postId, request.getLocale());

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/reply/slice/" + "{commentId}" + "/" + "{postId}" + "/" + "{page}")
    public String viewMoreReplys(@CurrentMember Member member, Model model, @PathVariable Long commentId, @PathVariable Long postId, @PathVariable int page) {

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "vote.upVote", "createdDate"));
        Slice<Comment> replys = commentRepository.findByParentId(commentId, pageRequest);

        setSliceAttributes(member, model, postId, replys);

        model.addAttribute("target", "reply");
        if(replys.isLast()) {
            model.addAttribute("itIsLastReplyPage", true);
        }

        model.addAttribute("commentId", commentId);
        model.addAttribute("itIsLastCommentPage", true);
        model.addAttribute("itIsReply", true);
        return "fragments :: comments";
    }

    @GetMapping("/comment/slice/" + "{postId}" + "/" + "{page}")
    public String viewMoreComments(@CurrentMember Member member, Model model, @PathVariable Long postId, @PathVariable int page) {

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "vote.upVote", "createdDate"));
        Slice<Comment> comments = commentRepository.findByPostIdAndParent(postId, null, pageRequest);

        setSliceAttributes(member, model, postId, comments);

        model.addAttribute("target", "comment");
        if(comments.isLast()) {
            model.addAttribute("itIsLastCommentPage", true);
        }

        return "fragments :: comments";
    }
    private void setSliceAttributes(@CurrentMember Member member, Model model, @PathVariable Long postId, Slice<Comment> replys) {
        if(member != null) {
            model.addAttribute(member);
        }

        model.addAttribute("postId", postId);
        model.addAttribute("comments", replys);
        model.addAttribute(new CommentForm());
    }
    private boolean isThereErrorsInCommentForm(Long id, CommentForm form, RedirectAttributes attributes) {
        boolean status = false;

        if(form.getFile().isEmpty() && form.getContent().isEmpty()) {
            attributes.addFlashAttribute("blankCommentForm", "코맨트를 입력하세요.");
            status = true;
        }

        if(!form.getFile().isEmpty() && !validateFile(form.getFile(), FileConfig.ACCEPTABLE_COMMENT_TYPES)) {
            attributes.addFlashAttribute("wrongFileType", "정의되지 않은 확장자입니다.");
            status = true;
        }
        if(!form.getFile().isEmpty() && form.getFile().getSize() >= 10485760) {
            attributes.addFlashAttribute("overSizedFile", "10mb 이하의 파일을 선택하세요.");
            status = true;
        }

        return status;
    }
    private boolean validateFile(MultipartFile file, String typesString) {
        // is file in type or not.
        return Arrays.stream(typesString.replace(" ", "").split(","))
                .anyMatch(type -> type.equals(file.getContentType()));
    }
}
