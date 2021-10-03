package dev.valium.sweetmeme.module.post_vote;

import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PostVoteController {

    private final PostVoteService postVoteService;
    private final MemberService memberService;

    @PostMapping("/post/{id}/vote")
    @ResponseBody
    public ResponseEntity votePost(@CurrentMember Member member, @RequestBody Map<String, String> params) throws Exception {
        Long id = Long.valueOf(params.get("id"));
        boolean vote = Boolean.parseBoolean(params.get("vote"));

        Member updatedMember = postVoteService.votePost(member, id, vote);
        memberService.updatePrincipal(updatedMember);

        return ResponseEntity.ok().build();
    }
}
