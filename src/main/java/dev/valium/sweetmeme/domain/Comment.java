package dev.valium.sweetmeme.domain;

import dev.valium.sweetmeme.domain.embeddable.Vote;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"id"})
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntityTime {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String commentedMember;
    private Vote vote;
    private String descriptionImg;
    @Column(nullable = false)
    private String content;


    // Post -> Comment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    // 재귀(트리) - 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent = null;
    
    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    // 쿼리 최적화를 위해 코맨트 수를 병행관리
    private int replyCount;
}
