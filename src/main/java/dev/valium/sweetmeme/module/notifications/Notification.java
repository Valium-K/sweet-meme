package dev.valium.sweetmeme.module.notifications;

import dev.valium.sweetmeme.module.bases.BaseEntityTime;
import dev.valium.sweetmeme.module.bases.BaseEntityZonedTime;
import dev.valium.sweetmeme.module.info.Info;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post.Post;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"notificationType", "memberId", "originalPostId"}, callSuper = false)
@Builder @AllArgsConstructor
@IdClass(NotificationId.class)
public class Notification extends BaseEntityZonedTime implements Serializable {

//    @Id @GeneratedValue
//    private Long id;

    private boolean hasRead;

    @Id @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @OneToOne(fetch = FetchType.LAZY)
    private Info lastVoteOrCommenter;

    private int voteOrCommentCounter;

    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime lastVoteOrCommentDate;
    /**
     * 알림을 받을 맴버 id
     */
    @Id @JoinColumn(name = "member_id")
    private Long memberId;

    @Id @JoinColumn(name = "original_post_id")
    private Long originalPostId;

    public Notification() {}
    public static Notification create(NotificationType type, Info info, Member member, Post originalPost) {
        return Notification.builder()
                .notificationType(type)
                .lastVoteOrCommenter(info)
                .memberId(member.getId())
                .originalPostId(originalPost.getId())
                .lastVoteOrCommentDate(ZonedDateTime.now())
                .build();
    }
}
