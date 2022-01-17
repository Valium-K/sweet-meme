package dev.valium.sweetmeme.module.tag;

import dev.valium.sweetmeme.module.bases.BaseEntityTime;
import dev.valium.sweetmeme.module.bases.BaseEntityZonedTime;
import dev.valium.sweetmeme.module.post_tag.PostTag;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = {"tagName"}, callSuper = false)
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntityZonedTime {

    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String tagName;

//    @OneToMany(mappedBy = "tag", cascade = CascadeType.PERSIST)
//    private Set<PostTag> postTags = new HashSet<>();

    public Tag(String tagName) {
        this.tagName = tagName;
    }
}
