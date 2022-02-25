package dev.valium.sweetmeme.module.comment_vote.commentvoter;

import dev.valium.sweetmeme.module.comment_vote.CommentVote;
import dev.valium.sweetmeme.module.comment_vote.CommentVoteRepository;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.notifications.NotificationType;
import dev.valium.sweetmeme.module.notifications.event.NotificationEvent;
import dev.valium.sweetmeme.module.post.Comment;
import dev.valium.sweetmeme.module.post.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.context.ApplicationEventPublisher;

@Builder
@AllArgsConstructor
public class DownVotedCommentVoter implements CommentVoter {
    private Comment comment;
    private Long id;
    private boolean vote;
    private Member member;
    private CommentVote downVotedComment;

    private final CommentVoteRepository commentVoteRepository;
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void voteComment() {
        member.getDownVotedIds().remove(id);

        if(vote) upVoteDownVotedComment(member, comment, downVotedComment);
        else downVoteDownVotedComment(member, id, comment, downVotedComment);
    }

    private void upVoteDownVotedComment(Member member, Comment comment, CommentVote downVotedComment) {
        commentVoteRepository.delete(downVotedComment);
        comment.getVote().subDownVote();
        comment.getVote().addUpVote();

        CommentVote commentVote = new CommentVote();
        commentVote.setUpVotedComment(comment);
        commentVote.setUpVotedMember(member);

        publishUpvoteCommentEvent(member, comment);

        commentVoteRepository.save(commentVote);
    }

    private void downVoteDownVotedComment(Member member, Long id, Comment comment, CommentVote downVotedComment) {
        comment.getVote().subDownVote();

        member.getUpVotedIds().add(id);
        commentVoteRepository.delete(downVotedComment);
    }

    private void publishUpvoteCommentEvent(Member member, Comment comment) {
        if (!member.getNickname().equals(comment.getCommenter()))
            eventPublisher.publishEvent(new NotificationEvent(comment.getPost(), member, NotificationType.UPVOTE_COMMENT));
    }
}
