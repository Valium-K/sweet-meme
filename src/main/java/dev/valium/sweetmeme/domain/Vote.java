package dev.valium.sweetmeme.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Builder
@AllArgsConstructor
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
}
