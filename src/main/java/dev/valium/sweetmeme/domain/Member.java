package dev.valium.sweetmeme.domain;

import dev.valium.sweetmeme.domain.enums.Membership;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"id"})
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntityTime {

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "info_id")
    private Info memberInfo;
    // member -> post
    @OneToMany(mappedBy = "originalPoster", cascade = CascadeType.ALL)
    private List<Post> myPosts = new ArrayList<>();
    // member -> post
    @OneToMany(mappedBy = "postedMember", cascade = CascadeType.ALL)
    private List<Post> commentedPosts = new ArrayList<>();
    // member -> post
    @OneToMany(mappedBy = "upVotedMember", cascade = CascadeType.ALL)
    private List<Post> upVotedPosts = new ArrayList<>();

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
                    .commentedPosts(new ArrayList<>())
                    .upVotedPosts(new ArrayList<>())
                    .build();
    }

    public int getSpendDate() {
        return Period.between(
                LocalDateTime.now().toLocalDate(),
                this.getCreatedDate().toLocalDate()
        ).getDays();
    }
}
