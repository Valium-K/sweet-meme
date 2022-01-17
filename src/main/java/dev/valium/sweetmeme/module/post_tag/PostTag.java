package dev.valium.sweetmeme.module.post_tag;

import dev.valium.sweetmeme.module.post.Post;
import dev.valium.sweetmeme.module.tag.Tag;
import lombok.*;

import javax.persistence.*;


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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public PostTag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
    }
}
