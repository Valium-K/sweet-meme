package dev.valium.sweetmeme.module.member;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberById(Long id);
    Member findMemberByNickname(String nickname);
    Member findMemberByEmail(String email);

    @EntityGraph(attributePaths = {"memberInfo"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    @Query("select m from Member m join fetch m.memberInfo where m.id = :id")
    Optional<Member> findFetchInfoById(@Param("id") Long id);
    @Query("select m from Member m join fetch m.memberInfo i where m.nickname = :nickname")
    Optional<Member> findMemberAndInfoByNickname(@Param("nickname") String nickname);
    @Query("select m from Member m join fetch m.memberInfo i where m.email = :email")
    Optional<Member> findMemberAndInfoByEmail(@Param("email") String email);
    @Query("select m from Member m join fetch m.commentedPosts where m.id = :id")
    Member findMemberAndCommentedPostsById(@Param("id") Long id);
}
