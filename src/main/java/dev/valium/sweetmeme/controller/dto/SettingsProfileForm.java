package dev.valium.sweetmeme.controller.dto;

import dev.valium.sweetmeme.domain.Member;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SettingsProfileForm {

    private String picImage;

    @Length(max = 100)
    private String description;
    private String state;

    public SettingsProfileForm(Member member, String state) {
        this.picImage = member.getMemberInfo().getPicImage();
        this.state = state;
        this.description = member.getMemberInfo().getDescription();
    }
}
