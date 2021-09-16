package dev.valium.sweetmeme.controller.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class SettingsPasswordForm {

    @NotBlank
    @Length(min = 8, max = 50)
    private String password;

    @NotBlank
    @Length(min = 8, max = 50)
    private String passwordConfirm;
}
