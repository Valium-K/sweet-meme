package dev.valium.sweetmeme.module.member.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class SettingsPasswordForm {

    @NotBlank
    @Length(min = 8, max = 50, message = "{common.password.help}")
    private String password;

    @NotBlank
    @Length(min = 8, max = 50, message = "{common.password.help}")
    private String passwordConfirm;
}
