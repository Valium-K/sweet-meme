package dev.valium.sweetmeme.module.notifications;

import dev.valium.sweetmeme.module.member.Member;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    public List<Notification> setAllHasRead(Member member) {

        try {
            List<Notification> notifications = notificationRepository.findByMemberId(member.getId());

            notifications.forEach(n -> n.setHasRead(true));

            return notifications;

        } catch (Exception e) {
            log.error("NotifcationService.setAllHasRead() - 알 수 없는 에러입니다.");
            return new ArrayList<>();
        }
    }

    public boolean removeAllHasRead(Member member) {

        try {
            return notificationRepository.deleteAllByMemberIdAndAndHasRead(member.getId(), true);

        } catch (Exception e) {
            log.error("NotifcationService.removeAllHasRead() - 알 수 없는 에러입니다.");
            return false;
        }
    }
}
