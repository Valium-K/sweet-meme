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
public class Post extends BaseEntityTime {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;

    @Embedded
    private Vote vote;

    // post -> tag
    @OneToMany(mappedBy = "post")
    private List<Tag> tags = new ArrayList<>();

    // post -> comment
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
    private int commentCount;

    // section -> post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;
    private SectionType belongedSectionType;

    // member -> post
    // 이 포스트를 업로드한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_poster_id")
    private Member originalPoster;

    // TODO N:M 이여따
    // member -> post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_voted_member_id")
    private Member upVotedMember;

    // member -> post
    // 멤버A 입장에서 봤을 때 이 포스트를 업로드한 사람
    // TODO 이름 더 잘 생각해보기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_member_id")
    private Member postedMember;


    public static Post createPost(String title, SectionType belongedSectionType) {
        return Post.builder()
                .title(title)
                .vote(new Vote())
                .tags(new ArrayList<>())
                .comments(new ArrayList<>())
                .belongedSectionType(belongedSectionType)
                .build();
    }
}
