package dev.valium.sweetmeme.domain;

import dev.valium.sweetmeme.domain.enums.Membership;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // 이메일 인증
    private boolean emailVerified;
    private String emailCheckToken;

    @Enumerated(EnumType.STRING)
    private Membership membership;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "info_id")
    private Info memberInfo;

    @OneToMany(mappedBy = "originalPoster")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "commentMember")
    private List<Comment> comments = new ArrayList<>();;

    @OneToMany(mappedBy = "upVotedMember")
    private List<Post> upVotedPosts = new ArrayList<>();;

    // TODO 스키마 여기만 새로 추가한 것 -> 나중에 다이어그램에 추가할 것

    public static Member createMember(String nickname, String email, String password) {
        return Member.builder()
                    .nickname(nickname)
                    .email(email)
                    .password(password)
                    .posts(new ArrayList<>())
                    .comments(new ArrayList<>())
                    .upVotedPosts(new ArrayList<>())
                    .build();
    }
}
