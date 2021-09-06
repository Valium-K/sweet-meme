package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.domain.CurrentMember;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.repository.PostRepository;
import dev.valium.sweetmeme.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class UploadController {

    private final MemberService memberService;

    @GetMapping("/upload")
    public String uploadForm() {

        return "upload";
    }

    @PostMapping("/upload")
    public String upload(@CurrentMember Member member) {

        // TODO Post를 form으로 받는다.
        // TODO form을 Post로 변환한다.

        // 테스트용 Post
        Post post = Post.createPost("testTitle", SectionType.FUNNY);
        memberService.uploadPost(member, post);



        return "redirect:/";
    }
}
