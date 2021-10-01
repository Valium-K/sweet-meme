package dev.valium.sweetmeme.module.member;

import dev.valium.sweetmeme.infra.config.FileConfig;
import dev.valium.sweetmeme.module.bases.BaseController;
import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post.Post;
import dev.valium.sweetmeme.module.post.PostRepository;
import dev.valium.sweetmeme.module.member.MemberService;
import dev.valium.sweetmeme.module.vote.VoteService;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

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


    @GetMapping("/comments")
    public String comments(@CurrentMember Member mem, @PathVariable String path, Model model) {
        Member member = setBaseProfile(path, model, mem, "comments");

        List<Post> posts = new ArrayList<>();
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
