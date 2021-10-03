package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.module.bases.enums.SectionType;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member_post.MemberPost;
import dev.valium.sweetmeme.module.post_tag.PostTag;
import dev.valium.sweetmeme.module.post_vote.PostVote;
import dev.valium.sweetmeme.module.section.Section;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"id"})
@Builder @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String postImageUrl;

    @Embedded
    private dev.valium.sweetmeme.module.bases.embeddable.Vote vote;

    // post <- post_tag -> tag
    @OneToMany(mappedBy = "post")
    private Set<PostTag> postTags = new HashSet<>();

    // post -> comment
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
    private int commentCount;

    @Enumerated(EnumType.STRING)
    private SectionType belongedSectionType;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

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
    @OneToMany(mappedBy = "upVotedPost")
    private List<PostVote> upVotedMembers;

//    // member -> post
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "down_voted_member_id")
//    private Member downVotedMember;

    @OneToMany(mappedBy = "commentedPost", cascade = CascadeType.ALL)
    private List<MemberPost> commentedPost;
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
                .vote(new dev.valium.sweetmeme.module.bases.embeddable.Vote())
                .postTags(new HashSet<>())
                .comments(new ArrayList<>())
                .belongedSectionType(belongedSectionType)
                .build();
    }

    public String getContentType() {
        return this.postImageUrl.split("\\.")[1];
    }
    public void addCommentCount() {
        this.commentCount++;
    }
    public void subCommentCount() {
        this.commentCount--;
    }
}
