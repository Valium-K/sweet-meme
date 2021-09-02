package dev.valium.sweetmeme.domain;

import dev.valium.sweetmeme.domain.enums.Membership;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"id"})
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Membership membership;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id")
    private Info memberInfo;

    @OneToMany(mappedBy = "originalPoster")
    private List<Post> posts;

    @OneToMany(mappedBy = "commentMember")
    private List<Comment> comments;

    @OneToMany(mappedBy = "upVotedMember")
    private List<Post> upVotedPosts;

    // TODO 스키마 여기만 새로 추가한 것 -> 나중에 다이어그램에 추가할 것

}
