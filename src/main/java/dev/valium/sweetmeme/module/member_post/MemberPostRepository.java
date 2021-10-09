package dev.valium.sweetmeme.module.member_post;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
public interface MemberPostRepository extends JpaRepository<MemberPost, Long> {

    List<MemberPost> findByCommentedMember(Member member);

    @EntityGraph(attributePaths = {"commentedPost"}, type = EntityGraph.EntityGraphType.LOAD)
    List<MemberPost> findFetchPostByCommentedMember(Member member);

    void deleteAllByCommentedPost(Post post);
    void deleteByCommentedMemberAndAndCommentedPost(Member member, Post post);
}
