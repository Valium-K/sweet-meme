package dev.valium.sweetmeme.domain;

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
     * Unique한 tagName을 tagId가 multiple 하게 참조함.
     * 그럴리 없지만 unique 제약조건에 위배될시 확인해볼것.
     */
    private Long tagId;

    public PostTag(Post post, Long tagId) {
        this.post = post;
        this.tagId = tagId;
    }
}
