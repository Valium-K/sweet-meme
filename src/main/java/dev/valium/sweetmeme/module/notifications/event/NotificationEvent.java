package dev.valium.sweetmeme.module.notifications.event;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.notifications.NotificationType;
import dev.valium.sweetmeme.module.post.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationEvent {

    private final Post post;
    private final Member member;
    private final NotificationType type;
}
