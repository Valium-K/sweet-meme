package dev.valium.sweetmeme.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @EqualsAndHashCode(of = {"tagName"})
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntityTime {

    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String tagName;

    // post -> tag
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id")
//    private Post post;

    public Tag(String tagName) {
        this.tagName = tagName;
    }
}
