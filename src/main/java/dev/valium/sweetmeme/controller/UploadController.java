package dev.valium.sweetmeme.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.valium.sweetmeme.controller.dto.SectionTagForm;
import dev.valium.sweetmeme.domain.CurrentMember;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.domain.Tag;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.repository.PostRepository;
import dev.valium.sweetmeme.repository.TagRepository;
import dev.valium.sweetmeme.service.MemberService;
import dev.valium.sweetmeme.service.UploadService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.hibernate.annotations.Parameter;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class UploadController {

    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;
    private final UploadService uploadService;


    @GetMapping("/upload")
    public String uploadForm(Model model) throws JsonProcessingException {

        List<String> tags = tagRepository.findAll().stream()
                .map(Tag::getTagName).collect(Collectors.toList());
        List<SectionType> sectionTypes = Arrays.stream(SectionType.values())
                .collect(Collectors.toList());

        model.addAttribute("tagWhitelist", objectMapper.writeValueAsString(tags));
        model.addAttribute("sectionWhitelist", objectMapper.writeValueAsString(sectionTypes));

        return "upload";
    }

    @PostMapping("/upload")
    public String upload(@CurrentMember Member member,
                         String title, String tags, String sections) throws JSONException {

        uploadService.uploadPost(member, title, tags, sections);

        return "redirect:/";
    }

    @PostMapping("/upload/section-tag/verify")
    @ResponseBody
    public ResponseEntity verifySectionTag(@RequestBody SectionTagForm form) {

        boolean anyMatch = Arrays.stream(SectionType.values())
                .map(SectionType::name)
                .anyMatch(s -> s.equals(form.getSectionName()));

        if(!anyMatch)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok().build();
    }

}
