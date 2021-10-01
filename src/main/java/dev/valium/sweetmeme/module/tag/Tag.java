package dev.valium.sweetmeme.module.tag;

import dev.valium.sweetmeme.module.bases.BaseEntityTime;
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

    // post <- post_tag -> tag
//    @OneToMany(mappedBy = "tag")
//    private Set<PostTag> postTags = new HashSet<>();

    public Tag(String tagName) {
        this.tagName = tagName;
    }
}
