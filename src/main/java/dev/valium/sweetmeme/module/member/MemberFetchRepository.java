package dev.valium.sweetmeme.module.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberFetchRepository {

//    @Query("select m from Member m join fetch m.memberInfo where m.id = :id")
//    Optional<Member> findFetchInfoById(@Param("id") Long id);
//
//    @Query("select m from Member m join fetch m.commentedPosts where m.id = :id")
//    Member findMemberAndCommentedPostsById(@Param("id") Long id);
}
