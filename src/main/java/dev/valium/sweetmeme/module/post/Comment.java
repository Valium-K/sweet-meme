package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.module.bases.BaseEntityTime;
import dev.valium.sweetmeme.module.bases.BaseEntityZonedTime;
import dev.valium.sweetmeme.module.bases.embeddable.Vote;
import dev.valium.sweetmeme.module.info.Info;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntityZonedTime {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "info_id")
    private Info commenterInfo;

    private Vote vote;

    private String descriptionImg;
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private boolean hasBeenDeleted;

    // 재귀(트리) - 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent = null;
    
    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    // 쿼리 최적화를 위해 코맨트 수를 병행관리
    private int replyCount;

    public static Comment create(Info info) {
        return Comment.builder()
                .commenterInfo(info)
                .vote(new Vote())
                .children(new ArrayList<>())
                .build();
    }

    public void addReplyCount() {
        this.replyCount++;
    }
    public void subReplyCount() {
        this.replyCount--;
    }
    public String getCommenter() {
        return this.getCommenterInfo().getHead();
    }
}
