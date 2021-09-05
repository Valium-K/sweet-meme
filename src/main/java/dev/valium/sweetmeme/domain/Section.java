package dev.valium.sweetmeme.domain;

import dev.valium.sweetmeme.domain.enums.SectionType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"id"})
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section extends BaseEntityTime {

    @Id @GeneratedValue
    @Column(name = "section_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SectionType sectionType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "section_info_id")
    private Info sectionInfo;

    // section -> post
    @OneToMany(mappedBy = "section")
    private List<Post> posts = new ArrayList<>();;
}
