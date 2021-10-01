package dev.valium.sweetmeme.module.comment_vote;

import dev.valium.sweetmeme.module.comment.Comment;
import dev.valium.sweetmeme.module.comment.CommentRepository;
import dev.valium.sweetmeme.module.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentVoteService {
    private final CommentVoteRepository commentVoteRepository;
    private final CommentRepository commentRepository;

    public boolean voteComment(Member member, Long id, boolean vote) throws Exception {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new Exception("comment 없음"));

        CommentVote upVotedComment = commentVoteRepository.findUpVoteByUpVotedMemberAndUpVotedComment(member, comment);
        //Comment upVotedComment = upvote.getUpVotedComment();

        CommentVote downVotedComment = commentVoteRepository.findDownVoteByDownVotedMemberAndDownVotedComment(member, comment);
        //Comment downVotedComment = downvote.getDownVotedComment();

        // 첫 보트
        if(downVotedComment == null && upVotedComment == null) {
            CommentVote commentVote = new CommentVote();

            if(vote) {
                commentVote.setUpVotedComment(comment);
                commentVote.setUpVotedMember(member);

                comment.getVote().addUpVote();
            }
            else {
                commentVote.setDownVotedComment(comment);
                commentVote.setDownVotedMember(member);

                comment.getVote().addDownVote();
            }
            commentVoteRepository.save(commentVote);
        }
        // upvote한 comment
        else if(upVotedComment != null && downVotedComment == null) {
            if(vote) {
                commentVoteRepository.delete(upVotedComment);
                comment.getVote().subUpvote();
            }
            else {
                commentVoteRepository.delete(upVotedComment);
                comment.getVote().subUpvote();
                comment.getVote().addDownVote();

                CommentVote commentVote = new CommentVote();
                commentVote.setDownVotedComment(comment);
                commentVote.setDownVotedMember(member);

                commentVoteRepository.save(commentVote);
            }

        }
        // downvote한 comment
        else {
            if(vote) {
                commentVoteRepository.delete(downVotedComment);
                comment.getVote().subDownVote();
                comment.getVote().addUpVote();

                CommentVote commentVote = new CommentVote();
                commentVote.setUpVotedComment(comment);
                commentVote.setUpVotedMember(member);

                commentVoteRepository.save(commentVote);
            }
            else {
                comment.getVote().subDownVote();
                commentVoteRepository.delete(downVotedComment);
            }
        }

        return true;
    }

    public List<Long> findUpVotedCommentsId(Member member) {
        List<CommentVote> commentVotes = commentVoteRepository.findByUpVotedMember(member);

        return commentVotes.stream()
                .map(CommentVote::getUpVotedComment)
                .map(Comment::getId)
                .collect(Collectors.toList());
    }

    public List<Long> findDownVotedCommentsId(Member member) {
        List<CommentVote> commentVotes = commentVoteRepository.findByDownVotedMember(member);

        return commentVotes.stream()
                .map(CommentVote::getDownVotedComment)
                .map(Comment::getId)
                .collect(Collectors.toList());
    }
}
