package dev.valium.sweetmeme.module.member.form;

import dev.valium.sweetmeme.module.member.Member;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SettingsAccountForm {

    @NotBlank
    @Length(min = 3, max = 20, message = "{common.nickname.error.length}")
    @Pattern(regexp = "^[a-z|A-Z|ㄱ-ㅎ|가-힣|0-9|_|-]{3,10}$", message = "{common.nickname.error.specialChar}")
    private String nickname;

    private boolean upvoteAlert;
    private boolean replyAlert;

    public SettingsAccountForm(Member member) {
        this.nickname = member.getNickname();
        this.upvoteAlert = member.isUpvoteAlert();
        this.replyAlert = member.isReplyAlert();
    }
}
