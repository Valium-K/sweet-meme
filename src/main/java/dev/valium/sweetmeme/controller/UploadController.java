package dev.valium.sweetmeme.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.valium.sweetmeme.controller.dto.SectionTagForm;
import dev.valium.sweetmeme.controller.dto.UploadForm;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.hibernate.annotations.Parameter;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UploadController {

    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;
    private final UploadService uploadService;

    @GetMapping("/upload")
    public String uploadForm(@CurrentMember Member member, Model model) throws Exception {

        List<String> tags = tagRepository.findAll().stream()
                .map(Tag::getTagName).collect(Collectors.toList());
        List<SectionType> sectionTypes = Arrays.stream(SectionType.values())
                .collect(Collectors.toList());

        model.addAttribute(member);
        model.addAttribute(new UploadForm());
        model.addAttribute("tagWhitelist", objectMapper.writeValueAsString(tags));
        model.addAttribute("sectionWhitelist", objectMapper.writeValueAsString(sectionTypes));

        return "upload";
    }


    @PostMapping("/upload")
    public String upload(@CurrentMember Member member, @Valid UploadForm form, BindingResult result) throws Exception {

        // TODO validation이 길어졌다. validation을 따로 추가해야겠다.
        if (result.hasErrors()) {
            return "upload";
        }

        if("".equals(form.getSections())) {
            // ajax로 체크함에도 오류로 null이 넘어오긴 힘들다.
            log.info("사용자의 의도적 에러 - section == null");
            return "redirect:/";
        }

        String firstSectionName = getSectionTypes(form.getSections()).get(0).name();
        if(!isSectionOnType(firstSectionName)) {
            // TODO controller에서 try-catch로 변경하기
            log.info("사용자의 의도적 에러 - form.section is not in sectionType");
            return "redirect:/";
        }
        // TODO 파일 검증

        uploadService.uploadPost(member, form.getTitle(), form.getTags(), form.getSections(), form.getFile());

        return "redirect:/";
    }

    @PostMapping("/upload/section-tag/verify")
    @ResponseBody
    public ResponseEntity verifySectionTag(@RequestBody SectionTagForm form) {

        if(!isSectionOnType(form.getSectionName()))
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok().build();
    }

    private List<SectionType> getSectionTypes(String jsonString) throws Exception {
        return uploadService.json2SectionTypeList(jsonString);
    }
    private boolean isSectionOnType(String sectionName) {
        boolean anyMatch = Arrays.stream(SectionType.values())
                .map(SectionType::name)
                .anyMatch(s -> s.equals(sectionName));
        return anyMatch;
    }
}
