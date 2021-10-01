package dev.valium.sweetmeme.module.comment;

import dev.valium.sweetmeme.module.comment.form.CommentForm;
import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static dev.valium.sweetmeme.infra.config.FileConfig.*;

/**
 * 코멘트와 코멘트 댓글관련 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping(COMMENT_IMAGE_URL + "{file}")
    @ResponseBody
    public ResponseEntity<byte[]> votePost(@PathVariable String file) throws Exception {

        InputStream imageStream = new FileInputStream(ABSOLUTE_COMMENT_IMAGE_PATH + file);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

//    @PostMapping("/reply/{postId}/{parentCommentId}")
//    public String postComment(@PathVariable Long postId, @PathVariable Long parentCommentId, Model model, @CurrentMember Member member,
//                              @Valid CommentForm form, BindingResult result) throws IOException {
//
//        if(form.getFile().isEmpty() && form.getContent().isEmpty()) {
////            model.addAttribute(member);
////            result.rejectValue("content", "comment.blankContentOrFile", new Object[]{},"코멘트나 파일을 작성");
//            return "redirect:/post/" + postId;
//        }
//        commentService.saveReply(postId, parentCommentId, form, member);
//
//        return "redirect:/post/" + postId;
//    }

//    @GetMapping("/reply/slice/" + "{commentId}" + "/" + "{page}")
//    public String viewReply(Model model, @PathVariable Long commentId, @PathVariable int page) {
//
//        model.addAttribute("replys", List.of(new ReplyDto(null, "new content.")));
//
//        System.out.println("asdf");
//
//
//        return "post/clickedPost :: #reply" + commentId;
//    }
}
