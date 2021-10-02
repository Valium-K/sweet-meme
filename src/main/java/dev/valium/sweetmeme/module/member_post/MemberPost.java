package dev.valium.sweetmeme.module.member_post;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post.Post;
import lombok.*;

import javax.persistence.*;

/**
 * [1:N:1] Member <-* member_post *-> Post
 * member가 post에 comment / reply를 작성할 경우
 * 해당 postId를 기억한다.
 */
@Entity
@Builder @Getter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class MemberPost {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commented_member_id")
    private Member commentedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commented_post_id")
    private Post commentedPost;

    public static MemberPost create(Member commentedMember, Post commentedPost) {
        return MemberPost.builder()
                .commentedMember(commentedMember)
                .commentedPost(commentedPost)
                .build();
    }
}
