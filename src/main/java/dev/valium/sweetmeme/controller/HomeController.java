package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.domain.CurrentMember;
import dev.valium.sweetmeme.domain.Info;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.repository.InfoRepository;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.repository.PostRepository;
import dev.valium.sweetmeme.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static dev.valium.sweetmeme.config.FileConfig.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostRepository postRepository;
    private final VoteService voteService;
    private final InfoRepository infoRepository;

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

    private void setBaseAttributes(Member member, Model model, String currentMenu) {
        if(member == null) {
            model.addAttribute("member", null);
        } else {
            model.addAttribute("member", member);

            List<Long> upVoteIds = voteService.findUpVotedPostsId(member);
            model.addAttribute("upVotedIds", upVoteIds);
            List<Long> downVoteIds = voteService.findDownVotedPostsId(member);
            model.addAttribute("downVotedIds", downVoteIds);
        }

        List<String> sectionTypes = Arrays.stream(SectionType.values()).map(s->s.name().toLowerCase()).collect(Collectors.toList());
        model.addAttribute("sidebarSectionTypes", sectionTypes);

        model.addAttribute("FILE_URL", FILE_URL);
        model.addAttribute("DOWNLOAD_URL", DOWNLOAD_URL);

        model.addAttribute("currentMenu", currentMenu);
    }
}
