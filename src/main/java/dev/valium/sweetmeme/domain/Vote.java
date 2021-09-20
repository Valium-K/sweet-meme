package dev.valium.sweetmeme.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"id"})
@Builder @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Vote {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upvoted_member_id")
    private Member upVotedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upvoted_post_id")
    private Post upVotedPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "downvoted_member_id")
    private Member downVotedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "downvoted_post_id")
    private Post downVotedPost;

    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(updatable = false)
    private LocalDateTime createdDate;
}
