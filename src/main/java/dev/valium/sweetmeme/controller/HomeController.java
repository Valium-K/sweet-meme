package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.domain.CurrentMember;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.repository.PostRepository;
import dev.valium.sweetmeme.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static dev.valium.sweetmeme.config.FileConfig.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostRepository postRepository;
    private final VoteService voteService;

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

        model.addAttribute("FILE_URL", FILE_URL);
        model.addAttribute("DOWNLOAD_URL", DOWNLOAD_URL);
        model.addAttribute("currentMenu", currentMenu);
        model.addAttribute("sectionType", null);
        model.addAttribute("info", currentMenu);
    }
}
