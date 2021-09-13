package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.config.fileConfig;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.repository.PostRepository;
import dev.valium.sweetmeme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.hibernate.annotations.Parameter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/{path}")
public class ProfileController {
    private final String ABSOLUTE_UPLOAD_PATH = fileConfig.ABSOLUTE_UPLOAD_PATH;

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @GetMapping("/home")
    public String home(@PathVariable String path, Model model) {
        Member member = setBaseProfile(path, model);

        return "user/home";
    }

    @GetMapping(value = "/posts", produces = MediaType.ALL_VALUE)
    public String posts(@PathVariable String path, Model model, HttpServletResponse response) throws IOException {
        Member member = setBaseProfile(path, model);

        List<Post> posts = postRepository.findAllByOriginalPoster(member);
        model.addAttribute("posts", posts);


        return "user/posts";
    }

    @GetMapping("/{file}")
    public ResponseEntity<byte[]> getImageAsByteArray(@PathVariable String file) throws IOException {
        InputStream imageStream = new FileInputStream(ABSOLUTE_UPLOAD_PATH + "/" + file);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }
    @GetMapping("/comments")
    public String comments(@PathVariable String path, Model model) {
        setBaseProfile(path, model);
        Member foundMember = memberService.findMemberAndInfo(path);
        model.addAttribute("spendDate", foundMember.getSpendDate());
        model.addAttribute("member", foundMember);

        return "user/comments";
    }

    @GetMapping("/upvotes")
    public String upvotes(@PathVariable String path, Model model) {
        setBaseProfile(path, model);

        Member foundMember = memberService.findMemberAndInfo(path);
        model.addAttribute("spendDate", foundMember.getSpendDate());
        model.addAttribute("member", foundMember);

        return "user/upvotes";
    }

    private Member setBaseProfile(String path, Model model) {
        Member foundMember = memberService.findMemberAndInfo(path);

        model.addAttribute("spendDate", foundMember.getSpendDate());
        model.addAttribute("info", foundMember.getMemberInfo());
        model.addAttribute("member", foundMember);

        return foundMember;
    }
}
