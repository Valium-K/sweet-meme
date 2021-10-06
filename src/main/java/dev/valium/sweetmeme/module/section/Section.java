package dev.valium.sweetmeme.module.section;

import dev.valium.sweetmeme.module.bases.BaseEntityTime;
import dev.valium.sweetmeme.module.bases.BaseEntityZonedTime;
import dev.valium.sweetmeme.module.bases.enums.SectionType;
import dev.valium.sweetmeme.module.info.Info;
import dev.valium.sweetmeme.module.post.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section extends BaseEntityZonedTime {

    @Id @GeneratedValue
    @Column(name = "section_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SectionType sectionType;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "section_info_id")
    private Info sectionInfo;

    @OneToMany(mappedBy = "section")
    private List<Post> posts = new ArrayList<>();

    public static Section createSection(SectionType sectionType, Info info) {
        return Section.builder()
                .sectionType(sectionType)
                .sectionInfo(info)
                .posts(new ArrayList<>())
                .build();
    }
}
