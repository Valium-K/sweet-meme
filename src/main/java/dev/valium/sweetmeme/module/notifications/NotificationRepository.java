package dev.valium.sweetmeme.module.notifications;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification, NotificationId> {

    Notification findByMemberIdAndOriginalPostIdAndNotificationType(Long memberId, Long originalPostId, NotificationType type);
    long countByMemberIdAndHasRead(Long memberId, boolean hasRead);

    @EntityGraph(attributePaths = {"lastVoteOrCommenter"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Notification> findByMemberId(Long memberId);

    @EntityGraph(attributePaths = {"lastVoteOrCommenter"}, type = EntityGraph.EntityGraphType.LOAD)
    Slice<Notification> findNotificationsByMemberIdAndHasRead(Long memberId, boolean hasRead, Pageable pageable);

    @EntityGraph(attributePaths = {"lastVoteOrCommenter"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Notification> findNotificationsByMemberIdAndHasRead(Long memberId, boolean hasRead);

    @EntityGraph(attributePaths = {"lastVoteOrCommenter"}, type = EntityGraph.EntityGraphType.LOAD)
    Slice<Notification> findNotificationsByMemberId(Long memberId, Pageable pageable);

    boolean deleteAllByMemberIdAndAndHasRead(Long memberId, boolean hasRead);
    void deleteAllByMemberId(Long memberId);
}
