package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.controller.dto.CommentForm;
import dev.valium.sweetmeme.domain.*;
import dev.valium.sweetmeme.repository.CommentRepository;
import dev.valium.sweetmeme.service.CommentService;
import dev.valium.sweetmeme.service.PostService;
import dev.valium.sweetmeme.service.TagService;
import dev.valium.sweetmeme.service.VoteService;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import static dev.valium.sweetmeme.config.FileConfig.*;

@Controller
public class PostController extends BaseController {

    private final PostService postService;
    private final TagService tagService;
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    public PostController(VoteService voteService, PostService postService, TagService tagService, CommentRepository commentRepository, CommentService commentService) {
        super(voteService);
        this.postService = postService;
        this.tagService = tagService;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
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
    public String currentPost(@PathVariable Long id, Model model, @CurrentMember Member member) {
        Post post = postService.findPostById(id);
        List<Comment> comments = commentRepository.findByPost(post);
        List<String> tags = tagService.findTags(post)
                            .stream()
                            .map(Tag::getTagName).collect(Collectors.toList());


        model.addAttribute(post);
        model.addAttribute("tags", tags);
        model.addAttribute("comments", comments );

        model.addAttribute(new CommentForm());

        setBaseAttributes(member, model, null);

        List<Long> upVoteCommentIds = commentService.findUpVotedCommentsId(member);
        model.addAttribute("upVoteCommentIds", upVoteCommentIds);
        List<Long> downVoteCommentIds = commentService.findDownVotedCommentsId(member);
        model.addAttribute("downVoteCommentIds", downVoteCommentIds);

        return "post/clickedPost";
    }

    @PostMapping("/post/{id}")
    public String postComment(@PathVariable Long id, Model model, @CurrentMember Member member,
                              @Valid CommentForm form, BindingResult result) throws IOException {

        commentService.saveComment(id, form, member);

        return "redirect:/post/" + id;
    }

    @PostMapping("/comment/{id}/vote")
    @ResponseBody
    public ResponseEntity votePost(@CurrentMember Member member, @RequestBody Map<String, String> params) throws Exception {
        Long id = Long.valueOf(params.get("id"));
        boolean vote = Boolean.parseBoolean(params.get("vote"));

        boolean success = commentService.voteComment(member, id, vote);
        if(success)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }
}
