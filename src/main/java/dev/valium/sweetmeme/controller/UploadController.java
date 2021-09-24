package dev.valium.sweetmeme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.valium.sweetmeme.controller.dto.SectionTagForm;
import dev.valium.sweetmeme.controller.dto.UploadForm;
import dev.valium.sweetmeme.controller.validator.UploadFormValidator;
import dev.valium.sweetmeme.domain.CurrentMember;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Tag;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.repository.TagRepository;
import dev.valium.sweetmeme.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UploadController {

    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;
    private final UploadService uploadService;
    private final UploadFormValidator uploadFormValidator;

    @InitBinder("uploadForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(uploadFormValidator);
    }

    @GetMapping("/upload/post")
    public String uploadForm(@CurrentMember Member member, Model model) throws Exception {

        List<String> tags = tagRepository.findAll().stream()
                .map(Tag::getTagName).collect(Collectors.toList());
        List<SectionType> sectionTypes = Arrays.stream(SectionType.values())
                .collect(Collectors.toList());

        model.addAttribute(member);
        model.addAttribute(new UploadForm());
        model.addAttribute("tagWhitelist", objectMapper.writeValueAsString(tags));
        model.addAttribute("sectionWhitelist", objectMapper.writeValueAsString(sectionTypes));
        
        model.addAttribute("sidebarSectionTypes", sectionTypes.stream().map(Enum::name).collect(Collectors.toList()));

        return "upload";
    }


    @PostMapping("/upload/post")
    public String upload(Model model, @CurrentMember Member member, @Valid UploadForm form, BindingResult result) throws Exception {


        if (result.hasErrors()) {
            log.error("error");
            model.addAttribute(member);
            return "upload";
        }

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
    private boolean isSectionOnType(String sectionName) {
        return Arrays.stream(SectionType.values())
                .map(SectionType::name)
                .anyMatch(s -> s.equals(sectionName));
    }
}
