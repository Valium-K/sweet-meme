package dev.valium.sweetmeme.module.post_vote;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostVoteRepository extends JpaRepository<PostVote, Long> {

    PostVote findUpVoteByUpVotedMemberAndUpVotedPost(Member member, Post post);
    PostVote findDownVoteByDownVotedMemberAndDownVotedPost(Member member, Post post);

    @Query("select v from PostVote v join fetch v.upVotedPost where v.upVotedMember = :member")
    List<PostVote> findByUpVotedMember(@Param("member") Member member);

    @Query("select v from PostVote v join fetch v.downVotedPost where v.downVotedMember = :member")
    List<PostVote> findByDownVotedMember(@Param("member") Member member);

    @Query("select v from PostVote v join fetch v.upVotedPost p join fetch v.upVotedMember where v.upVotedMember = :member order by v.createdDate desc")
    List<PostVote> findAllByUpVotedMember(@Param("member") Member member);
}