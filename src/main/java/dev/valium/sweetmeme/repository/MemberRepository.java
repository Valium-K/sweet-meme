package dev.valium.sweetmeme.repository;

import dev.valium.sweetmeme.domain.Info;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberById(Long id);
    Member findMemberByNickname(String nickname);
    Member findMemberByEmail(String email);

    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);

    @Query("select m from Member m join m.memberInfo i where m.nickname = :nickname and i.id = m.memberInfo.id")
    Optional<Member> findMemberAndInfoByNickname(@Param("nickname") String nickname);
}
