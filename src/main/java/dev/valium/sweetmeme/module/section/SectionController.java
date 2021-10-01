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
import dev.valium.sweetmeme.module.vote.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
public class SectionController  extends BaseController {

    private final PostRepository postRepository;
    private final InfoRepository infoRepository;

    public SectionController(VoteService voteService, PostRepository postRepository, InfoRepository infoRepository) {
        super(voteService);
        this.postRepository = postRepository;
        this.infoRepository = infoRepository;
    }

    @GetMapping("/fresh")
    public String fresh(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "fresh");

        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);

        return "home/home";
    }

    @GetMapping("/top")
    public String top(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "top");

        return "home/home";
    }

    @GetMapping("/funny")
    public String funny(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "funny");

        SectionType sectionType = SectionType.FUNNY;

        Info infoByHead = infoRepository.findInfoByHead(sectionType.name());
        model.addAttribute("sectionInfo", infoByHead);

        List<Post> posts = postRepository.findAllByBelongedSectionTypeOrderByCreatedDateDesc(sectionType);
        model.addAttribute("posts", posts);

        return "home/home";
    }

    @GetMapping("/animals")
    public String animals(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "animals");

        SectionType sectionType = SectionType.ANIMALS;

        Info infoByHead = infoRepository.findInfoByHead(sectionType.name());
        model.addAttribute("sectionInfo", infoByHead);

        List<Post> posts = postRepository.findAllByBelongedSectionTypeOrderByCreatedDateDesc(sectionType);
        model.addAttribute("posts", posts);

        return "home/home";
    }

    @GetMapping("/gif")
    public String gif(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "gif");

        SectionType sectionType = SectionType.GIF;

        Info infoByHead = infoRepository.findInfoByHead(sectionType.name());
        model.addAttribute("sectionInfo", infoByHead);

        List<Post> posts = postRepository.findAllByBelongedSectionTypeOrderByCreatedDateDesc(sectionType);
        model.addAttribute("posts", posts);

        return "home/home";
    }

    @GetMapping("/meme")
    public String meme(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "meme");

        SectionType sectionType = SectionType.MEME;

        Info infoByHead = infoRepository.findInfoByHead(sectionType.name());
        model.addAttribute("sectionInfo", infoByHead);

        List<Post> posts = postRepository.findAllByBelongedSectionTypeOrderByCreatedDateDesc(sectionType);
        model.addAttribute("posts", posts);

        return "home/home";
    }

    @GetMapping("/wholesome")
    public String wholesome(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "wholesome");

        SectionType sectionType = SectionType.WHOLESOME;

        Info infoByHead = infoRepository.findInfoByHead(sectionType.name());
        model.addAttribute("sectionInfo", infoByHead);

        List<Post> posts = postRepository.findAllByBelongedSectionTypeOrderByCreatedDateDesc(sectionType);
        model.addAttribute("posts", posts);

        return "home/home";
    }

    @GetMapping("/wallpaper")
    public String wallpaper(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "wallpaper");

        SectionType sectionType = SectionType.WALLPAPER;

        Info infoByHead = infoRepository.findInfoByHead(sectionType.name());
        model.addAttribute("sectionInfo", infoByHead);

        List<Post> posts = postRepository.findAllByBelongedSectionTypeOrderByCreatedDateDesc(sectionType);
        model.addAttribute("posts", posts);

        return "home/home";
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
