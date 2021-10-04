package dev.valium.sweetmeme.module.notifications;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Notification setNotificationHasRead(Long memberId, Long postId, NotificationType notificationType) {
        Notification notification = notificationRepository
                .findByMemberIdAndOriginalPostIdAndNotificationType(memberId, postId, notificationType);

        notification.setHasRead(true);

        return notification;
    }
}
