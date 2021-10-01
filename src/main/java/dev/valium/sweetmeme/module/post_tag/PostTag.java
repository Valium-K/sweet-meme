package dev.valium.sweetmeme.module.post_tag;

import dev.valium.sweetmeme.module.post.Post;
import lombok.*;

import javax.persistence.*;

@Entity(name = "post_tag")
@Setter @Getter
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * 1:N:1
     * Post - PostTag -> Tag
     *
     * Unique한 tagName을 tagId가 multiple 하게 참조함.
     */
    private Long tagId;

    public PostTag(Post post, Long tagId) {
        this.post = post;
        this.tagId = tagId;
    }
}
