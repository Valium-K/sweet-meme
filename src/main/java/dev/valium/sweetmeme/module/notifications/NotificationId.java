package dev.valium.sweetmeme.module.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationId implements Serializable {
    private NotificationType notificationType;
    private Long memberId;
    private Long originalPostId;
}