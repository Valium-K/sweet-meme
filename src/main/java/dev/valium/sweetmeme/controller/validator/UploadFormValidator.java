package dev.valium.sweetmeme.controller.validator;

import dev.valium.sweetmeme.controller.dto.UploadForm;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadFormValidator implements Validator {

    private final UploadService uploadService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UploadForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UploadForm form = (UploadForm) target;

        if("".equals(form.getSections())) {
            log.error("섹션이 null 입니다.");
            errors.rejectValue("sections", "invalid.section.null",
                    new Object[]{}, "섹션이 null 입니다.");
        }

        String firstSectionName = getSectionTypes(form.getSections()).get(0).name();

        if(!isSectionOnType(firstSectionName)) {
            log.error(form.getSections() + "은 정의된 섹션값이 아닙니다.");
            errors.rejectValue("sections", "invalid.section",
                    new Object[]{form.getSections()}, form.getSections() + "은 정의된 섹션값이 아닙니다.");
        }

        if(!validateFile(form.getFile(), form.ACCEPTABLE_FILE_TYPES)) {
            log.error(form.getFile().getContentType() + "은 정의된 확장자가 아닙니다.");
            errors.rejectValue("file", "invalid.file",
                    new Object[]{form.getFile().getContentType()}, form.getFile().getContentType() + "은 정의된 확장자가 아닙니다.");
        }
    }

    private boolean validateFile(MultipartFile file, String typesString) {
        if(file == null) {
            log.info("file in null");
            return false;
        }
        System.out.println("===============");
        Arrays.stream(typesString.replace(" ", "").split(",")).forEach(System.out::println);

        // is file in type or not.
        return Arrays.stream(typesString.replace(" ", "").split(","))
                .anyMatch(type -> type.equals(file.getContentType()));
    }
    private boolean isSectionOnType(String sectionName) {
        return Arrays.stream(SectionType.values())
                .map(SectionType::name)
                .anyMatch(s -> s.equals(sectionName));
    }
    private List<SectionType> getSectionTypes(String jsonString) {
        try {
            return uploadService.json2SectionTypeList(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
