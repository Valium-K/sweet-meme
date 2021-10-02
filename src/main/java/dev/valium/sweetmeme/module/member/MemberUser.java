package dev.valium.sweetmeme.module.member;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class MemberUser extends User {
    private Member member;

    public MemberUser(Member member) {
        super(member.getNickname(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.member = member;
    }
}
