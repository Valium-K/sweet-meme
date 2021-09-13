package dev.valium.sweetmeme.repository;

import dev.valium.sweetmeme.domain.Info;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @EntityGraph(attributePaths = {"memberInfo"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    @Query("select m from Member m join fetch m.memberInfo i where m.nickname = :nickname")
    Optional<Member> findMemberAndInfoByNickname(@Param("nickname") String nickname);

    @Query("select m from Member m join fetch m.memberInfo where m.id = :id")
    Optional<Member> findFetchInfoById(@Param("id") Long id);

}
