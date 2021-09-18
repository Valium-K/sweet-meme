package dev.valium.sweetmeme.controller.dto;

import dev.valium.sweetmeme.domain.Member;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SettingsProfileForm {

    private MultipartFile file = null;

    @Length(max = 100, message = "{common.description.error.length}")
    private String description;
    private String state;

    public SettingsProfileForm() {}
    public SettingsProfileForm(Member member, String state) {
        this.state = state;
        this.description = member.getMemberInfo().getDescription();
    }
}
