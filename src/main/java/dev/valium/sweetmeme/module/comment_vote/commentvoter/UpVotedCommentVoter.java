package dev.valium.sweetmeme.module.comment_vote.commentvoter;

import dev.valium.sweetmeme.module.comment_vote.CommentVote;
import dev.valium.sweetmeme.module.comment_vote.CommentVoteRepository;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post.Comment;
import dev.valium.sweetmeme.module.post.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.context.ApplicationEventPublisher;

@Builder
@AllArgsConstructor
public class UpVotedCommentVoter implements CommentVoter {
    private Comment comment;
    private Long id;
    private boolean vote;
    private Member member;
    private CommentVote upVotedComment;

    private final CommentVoteRepository commentVoteRepository;
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void voteComment() {
        member.getUpVotedIds().remove(id);

        upVoteUpVotedComment(comment, upVotedComment);
        if(!vote) downVoteUpVotedComment(member, id, comment);
    }

    private void upVoteUpVotedComment(Comment comment, CommentVote upVotedComment) {
        commentVoteRepository.delete(upVotedComment);
        comment.getVote().subUpvote();
    }

    private void downVoteUpVotedComment(Member member, Long id, Comment comment) {
        comment.getVote().addDownVote();

        CommentVote commentVote = new CommentVote();
        commentVote.setDownVotedComment(comment);
        commentVote.setDownVotedMember(member);

        member.getDownVotedIds().add(id);

        commentVoteRepository.save(commentVote);
    }
}
