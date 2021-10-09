package dev.valium.sweetmeme.module.post_tag;

import dev.valium.sweetmeme.module.post.Post;
import lombok.*;

import javax.persistence.*;

/**
 * [1:N:?] Post <-*> PostTag : [tagId 주석참조]
 */
@Entity(name = "post_tag")
@Setter @Getter
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * Unique한 tagName을 tagId가 multiple 하게 참조함.
     * 그래서 Tag객체가 아닌 tagId를 직접 알고 있음
     */
    private Long tagId;

    public PostTag(Post post, Long tagId) {
        this.post = post;
        this.tagId = tagId;
    }
}
