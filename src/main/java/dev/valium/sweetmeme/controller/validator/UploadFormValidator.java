package dev.valium.sweetmeme.controller.validator;

import dev.valium.sweetmeme.controller.dto.UploadForm;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UploadForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UploadForm form = (UploadForm) target;

        if(form.getSections() == null) {
            log.error("섹션이 null 입니다.");
            errors.rejectValue("sections", "upload.section.error.blank",
                    new Object[]{}, "섹션이 null 입니다.");
        }
        if(form.getFile() == null) {
            log.error("파일이 null 입니다.");
            errors.rejectValue("file", "upload.file.error.blank",
                    new Object[]{}, "파일이 null 입니다.");
        }
        String firstSectionName = getSectionTypes(form.getSections()).get(0);

        if(!isSectionOnType(firstSectionName)) {
            log.error(form.getSections() + "은 정의된 섹션값이 아닙니다.");
            errors.rejectValue("sections", "upload.sections.error.type",
                    new Object[]{form.getSections()}, form.getSections() + "은 정의된 섹션값이 아닙니다.");
        }

        if(!validateFile(form.getFile(), form.ACCEPTABLE_FILE_TYPES)) {
            log.error(form.getFile().getContentType() + "은 정의된 확장자가 아닙니다.");
            errors.rejectValue("file", "upload.file.error.type",
                    new Object[]{form.getFile().getContentType()}, form.getFile().getContentType() + "은 정의된 확장자가 아닙니다.");
        }
    }

    private boolean validateFile(MultipartFile file, String typesString) {
        if(file == null) {
            log.info("file in null");
            return false;
        }
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
    private List<String> getSectionTypes(String jsonString) {
        try {
            return json2SectionTypeStringList(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    private List<String> json2SectionTypeStringList(String json) {
        JSONArray jsonArray = new JSONArray(json);
        List<String> outputs = new ArrayList<>();

        List<String> sectionNames = Arrays.stream(SectionType.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        for(int i = 0; i < jsonArray.length(); i++) {
            String value = (String) jsonArray.getJSONObject(i).get("value");

            if(sectionNames.contains(value))
                outputs.add(SectionType.valueOf(value).name());
            else {
                outputs.add("invalid");
            }
        }

        return outputs;
    }
}
