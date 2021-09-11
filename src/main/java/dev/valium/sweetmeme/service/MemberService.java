package dev.valium.sweetmeme.service;

import dev.valium.sweetmeme.controller.dto.MemberUser;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.domain.Tag;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Member findReadOnlyMember(String nickname) {
        return findMember(nickname);
    }
    public Member findMember(String nickname) {
        return memberRepository.findByNickname(nickname).orElseThrow(
                () -> new IllegalArgumentException(nickname + "에 해당하는 멤버를 찾을 수 없습니다.")
        );
    }

    @Transactional(readOnly = true)
    public Member findMemberAndInfo(String nickname) {
        return memberRepository.findMemberAndInfoByNickname(nickname).orElseThrow(
            () -> new IllegalArgumentException(nickname + "에 해당하는 멤버를 찾을 수 없습니다.")
        );
    }

    public void login(Member member) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new MemberUser(member),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    public Member saveMember(Member member) {
        Member savedMember = memberRepository.save(member);

        return memberRepository.findFetchInfoById(savedMember.getId()).orElse(savedMember);
    }


    @Transactional(readOnly = true)
    public List<Post> findPostsByNickname(String nickname) {
        Member foundMember = findMember(nickname);

        return new ArrayList<>(foundMember.getMyPosts());
    }
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email)
        );

        return new MemberUser(member);
    }

}

