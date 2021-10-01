package dev.valium.sweetmeme.module.member.validator;

import dev.valium.sweetmeme.infra.config.FileConfig;
import dev.valium.sweetmeme.module.member.form.SettingsProfileForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettingsProfileValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SettingsProfileForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SettingsProfileForm form = (SettingsProfileForm) target;

        if(!form.getFile().isEmpty() && !validateFile(form.getFile(), FileConfig.ACCEPTABLE_AVATAR_TYPES)) {
            log.error(form.getFile().getContentType() + "은 정의된 확장자가 아닙니다.");
            errors.rejectValue("file", "upload.file.error.type",
                    new Object[]{form.getFile().getContentType()}, form.getFile().getContentType() + "은 정의된 확장자가 아닙니다.");
        }
    }

    private boolean validateFile(MultipartFile file, String typesString) {
        Arrays.stream(typesString.replace(" ", "").split(",")).forEach(System.out::println);

        // is file in type or not.
        return Arrays.stream(typesString.replace(" ", "").split(","))
                .anyMatch(type -> type.equals(file.getContentType()));
    }
}
