package dev.valium.sweetmeme.service;


import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.repository.MemberFetchRepository;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.service.exceptions.MemberException;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberFetchService {

    private final MemberFetchRepository memberFetchRepository;

    public Member findMemberFetchInfo(Member member) throws Exception {

        Optional<Member> foundMember = memberFetchRepository.findFetchInfoById(member.getId());

        return foundMember.orElseThrow(() ->
            new Exception("해당하는 멤버를 찾을 수 없습니다.")
        );
    }



}
