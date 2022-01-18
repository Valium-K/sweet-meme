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
@EqualsAndHashCode(of = {"notificationType", "originalPostId"}, callSuper = false)
@Builder @AllArgsConstructor
@IdClass(NotificationId.class)
public class Notification extends BaseEntityZonedTime implements Serializable {

    /**
     * "notificationType", "originalPostId"를 후보키로 잡았다.
     *
     * memberId가 쓴 originalPost의 notificationType으로 해당 member에게 알림을 띄운다.
     * 이 때, 알림은 notificationType 중 하나가 되며 그것의 counter를 저장하고 있고,
     * 마지막으로 Action을 한 사용자의 InfoId를 저장하고있다.
     *
     * 알림의 형식은 "[lastVoteOrCommenter]등 [voteOrCommentCounter]명이 [notificationType] 했습니다." 가된다.
     *
     */
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
