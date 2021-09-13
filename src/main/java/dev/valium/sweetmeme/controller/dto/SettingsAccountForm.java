package dev.valium.sweetmeme.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SettingsAccountForm {

    @NotBlank
    private String nickname;

    private boolean upvoteAlert;
    private boolean replyAlert;
}
