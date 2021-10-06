package dev.valium.sweetmeme.module.section;

import dev.valium.sweetmeme.module.bases.BaseController;
import dev.valium.sweetmeme.module.bases.enums.SectionType;
import dev.valium.sweetmeme.module.info.Info;
import dev.valium.sweetmeme.module.info.InfoRepository;
import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post.Post;
import dev.valium.sweetmeme.module.post.PostRepository;
import dev.valium.sweetmeme.module.section.form.SectionTagForm;
import dev.valium.sweetmeme.module.post_vote.PostVoteService;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static dev.valium.sweetmeme.infra.config.FileConfig.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SectionController  extends BaseController {

    private final PostRepository postRepository;
    private final InfoRepository infoRepository;

    @GetMapping("/")
    public String fresh(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "fresh");

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<Post> posts = postRepository.findAll(pageRequest);

        model.addAttribute("posts", posts);

        return "home/home";
    }
    @GetMapping("/post/slice/fresh/{page}")
    public String freshSlice(Model model, @PathVariable int page) {

        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<Post> posts = postRepository.findAll(pageRequest);

        model.addAttribute("posts", posts);
        model.addAttribute("FILE_URL", FILE_URL);

        return "fragments :: postSection";
    }

    @GetMapping("/hot")
    public String home(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "hot");

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "vote.upVote"));
        Slice<Post> posts = postRepository.findAllByCreatedDateBetween(LocalDateTime.now().minusHours(6), LocalDateTime.now(), pageRequest);

        model.addAttribute("posts", posts);

        return "home/home";
    }
    @GetMapping("/post/slice/hot/{page}")
    public String homeSlice(Model model, @PathVariable int page) {

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "vote.upVote"));
        Slice<Post> posts = postRepository.findAllByCreatedDateBetween(LocalDateTime.now().minusHours(6), LocalDateTime.now(), pageRequest);

        model.addAttribute("posts", posts);
        model.addAttribute("FILE_URL", FILE_URL);

        return "fragments :: postSection";
    }

    @GetMapping("/top")
    public String top(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "top");

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "vote.upVote"));
        Slice<Post> posts = postRepository.findAllByCreatedDateBetween(LocalDateTime.now().minusDays(3), LocalDateTime.now(), pageRequest);

        model.addAttribute("posts", posts);

        return "home/home";
    }
    @GetMapping("/post/slice/top/{page}")
    public String topSlice(Model model, @PathVariable int page) {

        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "vote.upVote"));
        Slice<Post> posts = postRepository.findAllByCreatedDateBetween(LocalDateTime.now().minusDays(3), LocalDateTime.now(), pageRequest);

        model.addAttribute("posts", posts);
        model.addAttribute("FILE_URL", FILE_URL);

        return "fragments :: postSection";
    }

    @GetMapping("/{section}")
    public String sectionPosts(@CurrentMember Member member, Model model, @PathVariable String section) {

        setBaseAttributes(member, model, section);
        SectionType sectionType;

        try {
            sectionType = Enum.valueOf(SectionType.class, section.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("SectionController: " + e.getMessage());
            return "home/home";
        }

        Info infoByHead = infoRepository.findInfoByHead(sectionType.name());
        model.addAttribute("sectionInfo", infoByHead);

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<Post> posts = postRepository.findAllByBelongedSectionType(sectionType, pageRequest);
        model.addAttribute("posts", posts);

        return "home/home";
    }
    @GetMapping("/post/slice/{section}/{page}")
    public String sectionSlice(Model model, @PathVariable String section, @PathVariable int page) {

        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "vote.upVote"));
        Slice<Post> posts = postRepository.findAllByBelongedSectionType(SectionType.valueOf(section.toUpperCase()), pageRequest);

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
}
