package dev.valium.sweetmeme.module.member;

import dev.valium.sweetmeme.infra.config.FileConfig;
import dev.valium.sweetmeme.module.processor.Code2State;
import dev.valium.sweetmeme.module.processor.FileProcessor;
import dev.valium.sweetmeme.module.member.form.SettingsAccountForm;
import dev.valium.sweetmeme.module.member.form.SettingsProfileForm;
import dev.valium.sweetmeme.module.post_vote.PostVoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostVoteService postVoteService;

    public Member findMember(String nickname) {
        // TODO Exception 구현
        return memberRepository.findByNickname(nickname).orElseThrow(
                () -> new IllegalArgumentException(nickname + "에 해당하는 멤버를 찾을 수 없습니다.")
        );
    }

    @Transactional(readOnly = true)
    // TODO Exception 구현
    public Member findMemberAndInfo(String nickname) {
        return memberRepository.findMemberAndInfoByNickname(nickname).orElseThrow(
            () -> new IllegalArgumentException(nickname + "에 해당하는 멤버를 찾을 수 없습니다.")
        );
    }

    // TODO upvotedList, downVotedList 함께 가져오기
    public void login(Member member) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new MemberUser(member),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    // TODO 프로필 수정 시, 코멘트시, up*down vote시 업뎃 후 세션에 보관하게 바꾸기
    public void updatePrincipal(Member member) {

        SecurityContext context = SecurityContextHolder.getContext();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new MemberUser(member),
                member.getPassword(),
                context.getAuthentication().getAuthorities()
        );

        context.setAuthentication(token);
    }

    public Member saveMember(Member member) {
        Member savedMember = memberRepository.save(member);

        return memberRepository.findFetchInfoById(savedMember.getId()).orElse(savedMember);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findMemberAndInfoByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email)
        );

        List<Long> upVoteIds = postVoteService.findUpVotedPostsId(member);
        member.setUpVotedIds(upVoteIds);
        List<Long> downVoteIds = postVoteService.findDownVotedPostsId(member);
        member.setDownVotedIds(downVoteIds);

        return new MemberUser(member);
    }

    // TODO Exception 구현
    public Member updatePassword(Member member, String pw) {
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException(member.getId() + "에 해당하는 멤버를 찾을 수 없습니다.")
        );

        foundMember.setPassword(passwordEncoder.encode(pw));
        updatePrincipal(foundMember);

        return foundMember;
    }

    // TODO Exception 구현
    public Member updateMemberAccount(Member member, SettingsAccountForm form) {
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException(member.getId() + "에 해당하는 멤버를 찾을 수 없습니다.")
        );

        foundMember.setNickname(form.getNickname());
        foundMember.setReplyAlert(form.isReplyAlert());
        foundMember.setUpvoteAlert(form.isUpvoteAlert());
        foundMember.getMemberInfo().setHead(form.getNickname());

        updatePrincipal(foundMember);

        return foundMember;
    }

    // TODO Exception 구현
    public Member updateProfile(Member member, SettingsProfileForm form) throws IOException {
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException(member.getId() + "에 해당하는 멤버를 찾을 수 없습니다.")
        );

        if(!form.getFile().isEmpty()) {
            String newFile = FileProcessor.transferFile(FileConfig.ABSOLUTE_AVATAR_PATH, form.getFile(), true);
            foundMember.getMemberInfo().setPicImage(newFile);
        }

        if("".equals(form.getDescription())) foundMember.getMemberInfo().setDescription(member.getNickname() + "'s description.");
        else foundMember.getMemberInfo().setDescription(form.getDescription());

        Code2State code2State = new Code2State();
        String code = code2State.json2Code(form.getState());
        if("".equals(code)) code = null;

        foundMember.getMemberInfo().setStateCode(code);
        updatePrincipal(foundMember);

        return foundMember;
    }

    // TODO Exception 구현
    public Member resetProfileAvatar(Member member) {
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException(member.getId() + "에 해당하는 멤버를 찾을 수 없습니다.")
        );

        foundMember.getMemberInfo().setPicImage(null);
        updatePrincipal(foundMember);

        return foundMember;
    }
}

