package dev.valium.sweetmeme.module.comment_vote;

import dev.valium.sweetmeme.module.comment.Comment;
import dev.valium.sweetmeme.module.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {

    CommentVote findUpVoteByUpVotedMemberAndUpVotedComment(Member member, Comment comment);
    CommentVote findDownVoteByDownVotedMemberAndDownVotedComment(Member member, Comment comment);

    @Query("select cv from CommentVote cv join fetch cv.upVotedComment cvu where cv.upVotedMember = :member")
    List<CommentVote> findByUpVotedMember(@Param("member") Member member);

    @Query("select cv from CommentVote cv join fetch cv.downVotedComment cvd where cv.downVotedMember = :member")
    List<CommentVote> findByDownVotedMember(@Param("member") Member member);
}
