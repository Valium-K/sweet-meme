package dev.valium.sweetmeme.service;

import com.zakgof.webp4j.Webp4j;
import dev.valium.sweetmeme.config.FileConfig;
import dev.valium.sweetmeme.controller.dto.MemberUser;
import dev.valium.sweetmeme.controller.dto.SettingsAccountForm;
import dev.valium.sweetmeme.controller.dto.SettingsProfileForm;
import dev.valium.sweetmeme.processor.Code2State;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.processor.FileProcessor;
import dev.valium.sweetmeme.repository.MemberFetchRepository;
import dev.valium.sweetmeme.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberFetchRepository memberFetchRepository;
    private final PasswordEncoder passwordEncoder;

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
                new MemberUser(memberFetchRepository.findFetchInfoById(member.getId()).orElse(member)),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    public void updatePrincipal(Member member) {
        SecurityContext context = SecurityContextHolder.getContext();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new MemberUser(memberFetchRepository.findFetchInfoById(member.getId()).orElse(member)),
                member.getPassword(),
                context.getAuthentication().getAuthorities()
        );

        context.setAuthentication(token);
    }

    public Member saveMember(Member member) {
        Member savedMember = memberRepository.save(member);

        return memberFetchRepository.findFetchInfoById(savedMember.getId()).orElse(savedMember);
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

    public Member updatePassword(Member member, String pw) {
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException(member.getId() + "에 해당하는 멤버를 찾을 수 없습니다.")
        );

        foundMember.setPassword(passwordEncoder.encode(pw));

        return foundMember;
    }

    public Member updateMemberAccount(Member member, SettingsAccountForm form) {
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException(member.getId() + "에 해당하는 멤버를 찾을 수 없습니다.")
        );

        foundMember.setNickname(form.getNickname());
        foundMember.setReplyAlert(form.isReplyAlert());
        foundMember.setUpvoteAlert(form.isUpvoteAlert());
        foundMember.getMemberInfo().setHead(form.getNickname());

        return foundMember;
    }

    public Member updateProfile(Member member, SettingsProfileForm form) throws IOException {
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException(member.getId() + "에 해당하는 멤버를 찾을 수 없습니다.")
        );

        if(!form.getFile().isEmpty()) {
            File newFile = FileProcessor.createNewFile(FileConfig.ABSOLUTE_AVATAR_PATH, form.getFile(), true);
            foundMember.getMemberInfo().setPicImage(newFile.getName());
            form.getFile().transferTo(newFile);
        }

        if("".equals(form.getDescription())) foundMember.getMemberInfo().setDescription(member.getNickname() + "'s description.");
        else foundMember.getMemberInfo().setDescription(form.getDescription());

        foundMember.getMemberInfo().setStateCode(Code2State.json2Code(form.getState()));

        return foundMember;
    }

    public Member resetProfileAvatar(Member member) {
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException(member.getId() + "에 해당하는 멤버를 찾을 수 없습니다.")
        );

        foundMember.getMemberInfo().setPicImage(null);

        return foundMember;
    }
}

