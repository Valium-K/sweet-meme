package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.module.bases.BaseController;
import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberService;
import dev.valium.sweetmeme.module.post_vote.PostVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/user/{path}")
@RequiredArgsConstructor
public class ProfileController extends BaseController {

    private final MemberService memberService;
    private final PostRepository postRepository;
    private final PostService postService;
    private final PostVoteService postVoteService;

    @GetMapping("/upvotes")
    public String profileUpVotes(@CurrentMember Member mem, @PathVariable String path, Model model) {
        Member member = setBaseProfile(path, model, mem, "upvotes");

        List<Post> posts = postVoteService.findUpVotedPosts(member);

        model.addAttribute("posts", posts);

        return "user/profile";
    }

    @GetMapping(value = "/posts")
    public String profilePosts(@CurrentMember Member mem, @PathVariable String path, Model model)  {
        Member member = setBaseProfile(path, model, mem, "posts");

        List<Post> posts = postRepository.findAllByOriginalPosterOrderByCreatedDateDesc(member);

        model.addAttribute("posts", posts);

        return "user/profile";
    }

    @GetMapping("/home")
    public String profileHome(@CurrentMember Member mem, @PathVariable String path, Model model) {
        Member member = setBaseProfile(path, model, mem, "home");

        List<Post> commentedPosts = postService.findFetchPostsByCommentedMember(member);
        List<Post> myPosts = postRepository.findAllByOriginalPosterOrderByCreatedDateDesc(member);
        List<Post> upVotedPosts = postVoteService.findUpVotedPosts(member);

        Set<Post> tempSet = new HashSet<>(commentedPosts);
        tempSet.addAll(myPosts);
        tempSet.addAll(upVotedPosts);

        List<Post> posts = new ArrayList<>(tempSet);

        model.addAttribute("posts", posts);

        return "user/profile";
    }


    @GetMapping("/comments")
    public String profileComments(@CurrentMember Member mem, @PathVariable String path, Model model) {
        Member member = setBaseProfile(path, model, mem, "comments");

        List<Post> posts = postService.findFetchPostsByCommentedMember(member);
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
