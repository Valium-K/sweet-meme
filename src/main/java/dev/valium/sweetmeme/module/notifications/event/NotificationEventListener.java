package dev.valium.sweetmeme.module.notifications.event;


import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.notifications.Notification;
import dev.valium.sweetmeme.module.notifications.NotificationRepository;
import dev.valium.sweetmeme.module.notifications.NotificationType;
import dev.valium.sweetmeme.module.post.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleNotificationEvent(NotificationEvent notificationEvent) {
        Member member = notificationEvent.getMember();
        Post post = notificationEvent.getPost();
        NotificationType type = notificationEvent.getType();

        if(type.equals(NotificationType.COMMENT) && post.getOriginalPoster().isReplyAlert()) {
            publishNotification(member, post, type);
        }
        else if(!type.equals(NotificationType.COMMENT) && post.getOriginalPoster().isUpvoteAlert()) {
            publishNotification(member, post, type);
        }
    }

    private void publishNotification(Member member, Post post, NotificationType type) {
        Notification foundNotification =
                notificationRepository.findByMemberIdAndOriginalPostIdAndNotificationType(member.getId()
                        , post.getId()
                        , type);

        if(foundNotification == null) {
            Notification notification =
                    Notification.create(type
                            , member.getMemberInfo()
                            , post.getOriginalPoster()
                            , post);

            setNotificationReadyToGo(member, post, notification, type);

            notificationRepository.save(notification);
        }
        else {
            setNotificationReadyToGo(member, post, foundNotification, type);
        }
    }
    private void setNotificationReadyToGo(Member member, Post post, Notification foundNotification, NotificationType type) {
        foundNotification.setHasRead(false);
        foundNotification.setLastVoteOrCommenter(member.getMemberInfo());
        if(type.name().equals(NotificationType.COMMENT)) {
            foundNotification.setVoteOrCommentCounter(post.getCommentCount());
        }
        else {
            foundNotification.setVoteOrCommentCounter(post.getVote().getUpVote());
        }

    }
}
