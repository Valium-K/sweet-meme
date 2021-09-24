package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.config.FileConfig;
import dev.valium.sweetmeme.domain.CurrentMember;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.repository.PostRepository;
import dev.valium.sweetmeme.service.MemberService;
import dev.valium.sweetmeme.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static dev.valium.sweetmeme.config.FileConfig.*;

@Controller
@RequestMapping("/user/{path}")
public class ProfileController extends BaseController {
    private final String ABSOLUTE_UPLOAD_PATH = FileConfig.ABSOLUTE_UPLOAD_PATH;

    private final MemberService memberService;
    private final PostRepository postRepository;
    private final VoteService voteService;

    public ProfileController(MemberService memberService, PostRepository postRepository, VoteService voteService) {
        super(voteService);
        this.voteService = voteService;
        this.memberService = memberService;
        this.postRepository = postRepository;
    }

    @GetMapping("/home")
    public String home(@CurrentMember Member mem, @PathVariable String path, Model model) {
        Member member = setBaseProfile(path, model, mem, "home");

        // TODO comment, upvote 구현필요
        List<Post> posts = new ArrayList<>();
        model.addAttribute("posts", posts);

        return "user/profile";
    }

    @GetMapping(value = "/posts", produces = MediaType.ALL_VALUE)
    public String posts(@CurrentMember Member mem, @PathVariable String path, Model model)  {
        Member member = setBaseProfile(path, model, mem, "posts");

        List<Post> posts = postRepository.findAllByOriginalPosterOrderByCreatedDateDesc(member);

        model.addAttribute("posts", posts);

        return "user/profile";
    }


    @GetMapping("/comments")
    public String comments(@CurrentMember Member mem, @PathVariable String path, Model model) {
        Member member = setBaseProfile(path, model, mem, "comments");

        List<Post> posts = new ArrayList<>();
        model.addAttribute("posts", posts);

        return "user/profile";
    }

    @GetMapping("/upvotes")
    public String upvotes(@CurrentMember Member mem, @PathVariable String path, Model model) {
        Member member = setBaseProfile(path, model, mem, "upvotes");

        List<Post> posts = voteService.findUpVotedPosts(member);

        model.addAttribute("posts", posts);

        return "user/profile";
    }



    private Member setBaseProfile(String path, Model model, Member member, String profileMenu) {
        Member foundMember = memberService.findMemberAndInfo(path);

        model.addAttribute("spendDate", foundMember.getSpendDate());
        model.addAttribute("info", foundMember.getMemberInfo());
        model.addAttribute("member", foundMember);

        setBaseAttributes(member, model, null);

        model.addAttribute("profileMenu", profileMenu);

        return foundMember;
    }
}
