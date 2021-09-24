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
public class CommentVote {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upvoted_member_id")
    private Member upVotedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upvoted_comment_id")
    private Comment upVotedComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "downvoted_member_id")
    private Member downVotedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "downvoted_comment_id")
    private Comment downVotedComment;
}
