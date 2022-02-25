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
public class FirstVotedCommentVoter implements CommentVoter {
    private Comment comment;
    private Long id;
    private boolean vote;
    private Member member;

    private final CommentVoteRepository commentVoteRepository;
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void voteComment() {
        CommentVote commentVote = new CommentVote();

        if(vote) setFirstUpVote(member, id, comment, commentVote);
        else setFirstDownVote(member, id, comment, commentVote);

        commentVoteRepository.save(commentVote);
    }

    private void setFirstUpVote(Member member, Long id, Comment comment, CommentVote commentVote) {
        commentVote.setUpVotedComment(comment);
        commentVote.setUpVotedMember(member);

        comment.getVote().addUpVote();

        member.getUpVotedIds().add(id);

        publishUpvoteCommentEvent(member, comment);
    }

    private void setFirstDownVote(Member member, Long id, Comment comment, CommentVote commentVote) {
        commentVote.setDownVotedComment(comment);
        commentVote.setDownVotedMember(member);

        comment.getVote().addDownVote();

        member.getDownVotedIds().add(id);
    }

    private void publishUpvoteCommentEvent(Member member, Comment comment) {
        if (!member.getNickname().equals(comment.getCommenter()))
            eventPublisher.publishEvent(new NotificationEvent(comment.getPost(), member, NotificationType.UPVOTE_COMMENT));
    }
}
