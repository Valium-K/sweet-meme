package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.domain.*;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.repository.CommentRepository;
import dev.valium.sweetmeme.repository.InfoRepository;
import dev.valium.sweetmeme.repository.PostRepository;
import dev.valium.sweetmeme.service.VoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController extends BaseController {

    private final PostRepository postRepository;
    private final InfoRepository infoRepository;
    private final CommentRepository commentRepository;

    public HomeController(VoteService voteService, PostRepository postRepository, InfoRepository infoRepository, CommentRepository commentRepository) {
        super(voteService);
        this.postRepository = postRepository;
        this.infoRepository = infoRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/test")
    public String t(Model model) {
        model.addAttribute("cId", "22");
        return "test";
    }

    @GetMapping("/test/{id}")
    public String t2(@PathVariable Long id, Model model) {
        System.out.println("sdfsdf");
        Comment parent = commentRepository.findCommentById(id);
        List<Comment> replys = commentRepository.findByParent(parent);

        model.addAttribute("replys", replys);

        return "fragments :: test";
    }

    @GetMapping
    public String home(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "hot");

        return "home/home";
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
}
