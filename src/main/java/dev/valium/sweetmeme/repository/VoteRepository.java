package dev.valium.sweetmeme.repository;

import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Vote findUpVoteByUpVotedMemberAndUpVotedPost(Member member, Post post);
    Vote findDownVoteByDownVotedMemberAndDownVotedPost(Member member, Post post);

    @Query("select v from Vote v join fetch v.upVotedPost where v.upVotedMember = :member")
    List<Vote> findByUpVotedMember(@Param("member") Member member);

    @Query("select v from Vote v join fetch v.downVotedPost where v.downVotedMember = :member")
    List<Vote> findByDownVotedMember(@Param("member") Member member);

    @Query("select v from Vote v join fetch v.upVotedPost p join fetch v.upVotedMember where v.upVotedMember = :member order by v.createdDate desc")
    List<Vote> findAllByUpVotedMember(@Param("member") Member member);
}