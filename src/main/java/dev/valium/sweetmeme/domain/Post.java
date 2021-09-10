package dev.valium.sweetmeme.domain;

import dev.valium.sweetmeme.domain.enums.SectionType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"id"})
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntityTime {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String postImageUrl;

    @Embedded
    private Vote vote;

    // post <- post_tag -> tag
    @OneToMany(mappedBy = "post")
    private Set<PostTag> postTags = new HashSet<>();

    // post -> comment
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
    private int commentCount;

    @Enumerated(EnumType.STRING)
    private SectionType belongedSectionType;

    ///////////////////////////////

    // section -> post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    // member -> post
    // 이 포스트를 업로드한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_poster_id")
    private Member originalPoster;

    // 1:N 단방향 매핑을 N:1 양방향 매핑으로 해석
    ////////////////////////////////////////////////////
    // member -> post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_voted_member_id")
    private Member upVotedMember;

    // member -> post
    // 멤버A 입장에서 봤을 때 이 포스트를 업로드한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_member_id")
    private Member postedMember;
    ////////////////////////////////////////////////////

    /**
     * create이후 section, op, postImageUrl 매핑 필요 + tag가 있을경우 추가로 매핑
     * @param title
     * @param belongedSectionType
     * @return
     */
    public static Post createPost(String title, SectionType belongedSectionType) {
        return Post.builder()
                .title(title)
                .vote(new Vote())
                .postTags(new HashSet<>())
                .comments(new ArrayList<>())
                .belongedSectionType(belongedSectionType)
                .build();
    }
}
