package dev.valium.sweetmeme.module.section;

import dev.valium.sweetmeme.module.bases.BaseController;
import dev.valium.sweetmeme.module.section.enums.SectionType;
import dev.valium.sweetmeme.module.info.Info;
import dev.valium.sweetmeme.module.info.InfoRepository;
import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post.Post;
import dev.valium.sweetmeme.module.post.PostRepository;
import dev.valium.sweetmeme.module.post_tag.PostTag;
import dev.valium.sweetmeme.module.post_tag.PostTagRepository;
import dev.valium.sweetmeme.module.section.form.SectionTagForm;
import dev.valium.sweetmeme.module.tag.Tag;
import dev.valium.sweetmeme.module.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static dev.valium.sweetmeme.infra.config.FileConfig.*;
import static dev.valium.sweetmeme.infra.config.WebConfig.SLICE_SIZE;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SectionController extends BaseController {

    private final PostRepository postRepository;
    private final InfoRepository infoRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    @GetMapping("/")
    public String fresh(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "fresh");

        PageRequest pageRequest = PageRequest.of(0, SLICE_SIZE, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<Post> posts = postRepository.findAll(pageRequest).toList();

        model.addAttribute("posts", posts);

        return "home/home";
    }
    @GetMapping("/post/slice/fresh/{page}")
    public String freshSlice(@CurrentMember Member member, Model model, @PathVariable int page) {

        setBaseAttributes(member, model, "fresh");

        PageRequest pageRequest = PageRequest.of(page, SLICE_SIZE, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<Post> posts = postRepository.findAll(pageRequest).toList();

        model.addAttribute("posts", posts);
        model.addAttribute("FILE_URL", FILE_URL);

        return "fragments :: postSection";
    }

    @GetMapping("/hot")
    public String home(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "hot");

        PageRequest pageRequest = PageRequest.of(0, SLICE_SIZE, Sort.by(Sort.Direction.DESC, "vote.upVote", "createdDate"));
        List<Post> posts = postRepository.findAllByCreatedDateBetween(LocalDateTime.now().minusHours(6), LocalDateTime.now(), pageRequest).toList();

        model.addAttribute("posts", posts);

        return "home/home";
    }
    @GetMapping("/post/slice/hot/{page}")
    public String homeSlice(@CurrentMember Member member, Model model, @PathVariable int page) {
        setBaseAttributes(member, model, "fresh");
        PageRequest pageRequest = PageRequest.of(page, SLICE_SIZE, Sort.by(Sort.Direction.DESC, "vote.upVote", "createdDate"));
        List<Post> posts = postRepository.findAllByCreatedDateBetween(LocalDateTime.now().minusHours(6), LocalDateTime.now(), pageRequest).toList();

        model.addAttribute("posts", posts);
        model.addAttribute("FILE_URL", FILE_URL);

        return "fragments :: postSection";
    }

    @GetMapping("/top")
    public String top(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "top");

        PageRequest pageRequest = PageRequest.of(0, SLICE_SIZE, Sort.by(Sort.Direction.DESC, "vote.upVote", "createdDate"));
        List<Post> posts = postRepository.findAllByCreatedDateBetween(LocalDateTime.now().minusYears(1L), LocalDateTime.now(), pageRequest).toList();

        model.addAttribute("posts", posts);

        return "home/home";
    }
    @GetMapping("/post/slice/top/{page}")
    public String topSlice(@CurrentMember Member member, Model model, @PathVariable int page) {
        setBaseAttributes(member, model, "fresh");
        PageRequest pageRequest = PageRequest.of(page, SLICE_SIZE, Sort.by(Sort.Direction.DESC, "vote.upVote", "createdDate"));
        List<Post> posts = postRepository.findAllByCreatedDateBetween(LocalDateTime.now().minusYears(1L), LocalDateTime.now(), pageRequest).toList();

        model.addAttribute("posts", posts);
        model.addAttribute("FILE_URL", FILE_URL);

        return "fragments :: postSection";
    }

    @GetMapping("/{sectionOrTag}")
    public String sectionPosts(@CurrentMember Member member, Model model, @PathVariable String sectionOrTag) {

        setBaseAttributes(member, model, sectionOrTag);
        SectionType sectionType = null;
        List<Post> posts;

        try {
            sectionType = SectionType.valueOf(sectionOrTag.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("sectionController.sectionPosts: " + sectionOrTag + "는 없습니다.");
        }

        // 섹션에서 찾아보기
        if(sectionType != null) {
            PageRequest pageRequest = PageRequest.of(0, SLICE_SIZE, Sort.by(Sort.Direction.DESC, "createdDate"));
            posts = postRepository.findAllByBelongedSectionType(sectionType, pageRequest);

            Info infoByHead = infoRepository.findInfoByHead(sectionType.name());
            model.addAttribute("sectionInfo", infoByHead);
        }
        // tag에서 찾아보기
        else {
            posts = getPostSliceByTagName(0, sectionOrTag);
        }

        model.addAttribute("posts", posts);

        return "home/home";
    }
    @GetMapping("/post/slice/{sectionOrTag}/{page}")
    public String sectionSlice(@CurrentMember Member member, Model model, @PathVariable String sectionOrTag, @PathVariable int page) {
        setBaseAttributes(member, model, "fresh");
        SectionType sectionType = null;

        try {
            sectionType = SectionType.valueOf(sectionOrTag.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("sectionController.sectionSlice: " + sectionOrTag + "는 없습니다.");
        }

        List<Post> posts;
        if(sectionType != null) {
            PageRequest pageRequest = PageRequest.of(page, SLICE_SIZE, Sort.by(Sort.Direction.DESC, "createdDate"));
            posts = postRepository.findAllByBelongedSectionType(sectionType, pageRequest);
        }
        else {
            posts = getPostSliceByTagName(page, sectionOrTag);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("FILE_URL", FILE_URL);

        return "fragments :: postSection";
    }

    @GetMapping(SECTION_URL + "{sectionName}")
    public ResponseEntity<byte[]> getSectionPic(@PathVariable String sectionName) throws IOException {

        InputStream imageStream = new FileInputStream(ABSOLUTE_SECTION_PATH + sectionName.toLowerCase() + ".jpg");
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    @PostMapping("/upload/section-tag/verify")
    @ResponseBody
    public ResponseEntity verifySectionTag(@RequestBody SectionTagForm form) {

        if(!isSectionOnType(form.getSectionName()))
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok().build();
    }
    private boolean isSectionOnType(String sectionName) {
        return Arrays.stream(SectionType.values())
                .map(SectionType::name)
                .anyMatch(s -> s.equals(sectionName));
    }

    private List<Post> getPostSliceByTagName(int start, String tagName) {
        // TODO tagName verify
        Tag tag = tagRepository.findByTagName(tagName);

        if(tag == null) {
            return new ArrayList<>();
        }

        Pageable pageRequest = PageRequest.of(start, SLICE_SIZE, Sort.by(Sort.Direction.DESC, "post.createdDate"));
        List<PostTag> postTags = postTagRepository.findPostTagsByTagId(tag.getId(), pageRequest);

        return postTags.stream().map(PostTag::getPost).collect(Collectors.toList());
    }
}
