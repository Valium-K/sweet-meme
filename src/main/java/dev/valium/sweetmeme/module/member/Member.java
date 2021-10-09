package dev.valium.sweetmeme.module.member;

import dev.valium.sweetmeme.module.bases.BaseEntityTime;
import dev.valium.sweetmeme.module.bases.BaseEntityZonedTime;
import dev.valium.sweetmeme.module.bases.enums.Membership;
import dev.valium.sweetmeme.module.comment_vote.CommentVote;
import dev.valium.sweetmeme.module.info.Info;
import dev.valium.sweetmeme.module.member_post.MemberPost;
import dev.valium.sweetmeme.module.post.Post;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntityZonedTime {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;
    private String password;

    private boolean emailVerified;
    private String emailCheckToken;

    private boolean upvoteAlert;
    private boolean replyAlert;

    @Enumerated(EnumType.STRING)
    private Membership membership;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "info_id")
    private Info memberInfo;

    @OneToMany(mappedBy = "originalPoster", cascade = CascadeType.ALL)
    private List<Post> myPosts = new ArrayList<>();

    @OneToMany(mappedBy = "commentedMember", cascade = CascadeType.ALL)
    private List<MemberPost> commentedMember = new ArrayList<>();

    @OneToMany(mappedBy = "upVotedMember", cascade = CascadeType.ALL)
    private List<CommentVote> upVotedComments = new ArrayList<>();
    @OneToMany(mappedBy = "downVotedMember", cascade = CascadeType.ALL)
    private List<CommentVote> downVotedComments = new ArrayList<>();

    @Transient private List<Long> upVotedIds = new ArrayList<>();
    @Transient private List<Long> downVotedIds = new ArrayList<>();
    @Transient private List<Long> upVoteCommentIds = new ArrayList<>();
    @Transient private List<Long> downVoteCommentIds = new ArrayList<>();
    @Transient private List<Long> CommentedIds = new ArrayList<>();

    public static String createEmailCheckToken() {
        return UUID.randomUUID().toString();
    }

    public static Member createMember(String nickname, String email, String password) {
        return Member.builder()
                    .nickname(nickname)
                    .email(email)
                    .password(password)
                    .emailVerified(false)
                    .membership(Membership.NEW)
                    .upvoteAlert(true)
                    .replyAlert(true)
                    .emailCheckToken(createEmailCheckToken())
                    .myPosts(new ArrayList<>())
                    .commentedMember(new ArrayList<>())
                    .upVotedIds(new ArrayList<>())
                    .downVotedIds(new ArrayList<>())
                    .upVoteCommentIds(new ArrayList<>())
                    .downVoteCommentIds(new ArrayList<>())
                    .build();
    }

    public int getSpendDate() {
        return Period.between(
                this.getCreatedDate().toLocalDate(),
                LocalDateTime.now().toLocalDate()
        ).getDays();
    }
}
