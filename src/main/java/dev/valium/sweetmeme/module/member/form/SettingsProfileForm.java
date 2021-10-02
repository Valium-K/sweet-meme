package dev.valium.sweetmeme.module.member.form;

import dev.valium.sweetmeme.module.member.Member;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SettingsProfileForm {

    private MultipartFile file = null;

    @Length(max = 100, message = "{common.description.error.length}")
    private String description;
    private String state;

    public SettingsProfileForm(Member member, String state) {
        this.state = state;
        this.description = member.getMemberInfo().getDescription();
    }
}
