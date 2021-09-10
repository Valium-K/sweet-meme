package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/{path}")
public class ProfileController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/home")
    public String home(@PathVariable String path, Model model) {
        Member foundMember = memberService.findMemberAndInfo(path);
        model.addAttribute("spendDate", foundMember.getSpendDate());
        model.addAttribute("member", foundMember);

        return "user/home";
    }

    @GetMapping("/posts")
    public String posts(@PathVariable String path, Model model) {
        Member foundMember = memberService.findMemberAndInfo(path);
        model.addAttribute("spendDate", foundMember.getSpendDate());
        model.addAttribute("member", foundMember);

        List<Post> foundPosts = memberService.findPostsByNickname(path);

        model.addAttribute("posts", foundPosts);

        return "user/posts";
    }

    @GetMapping("/comments")
    public String comments(@PathVariable String path, Model model) {
        Member foundMember = memberService.findMemberAndInfo(path);
        model.addAttribute("spendDate", foundMember.getSpendDate());
        model.addAttribute("member", foundMember);

        return "user/comments";
    }

    @GetMapping("/upvotes")
    public String upvotes(@PathVariable String path, Model model) {
        Member foundMember = memberService.findMemberAndInfo(path);
        model.addAttribute("spendDate", foundMember.getSpendDate());
        model.addAttribute("member", foundMember);

        return "user/upvotes";
    }

    private Model setBaseProfile(String path, Model model) {
        Member foundMember = memberService.findMemberAndInfo(path);
        model.addAttribute("spendDate", foundMember.getSpendDate());
        model.addAttribute("member", foundMember);

        return model;
    }
}
