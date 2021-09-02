package dev.valium.sweetmeme.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"id"})
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntityTime {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String Title;

    @OneToMany(mappedBy = "post")
    private List<Tag> tags = new ArrayList<>();;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @Embedded
    private Vote vote;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();;
    private int commentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_poster_id")
    private Member originalPoster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_voted_member_id")
    private Member upVotedMember;
}
