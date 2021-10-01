package dev.valium.sweetmeme.module.vote;

import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/post/{id}/vote")
    @ResponseBody
    public ResponseEntity votePost(@CurrentMember Member member, @RequestBody Map<String, String> params) throws Exception {
        Long id = Long.valueOf(params.get("id"));
        boolean vote = Boolean.parseBoolean(params.get("vote"));

        boolean success = voteService.votePost(member, id, vote);
        if(success)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }
}
