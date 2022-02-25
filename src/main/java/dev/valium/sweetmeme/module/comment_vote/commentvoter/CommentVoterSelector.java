package dev.valium.sweetmeme.module.comment_vote.commentvoter;

import dev.valium.sweetmeme.module.comment_vote.CommentVote;
import dev.valium.sweetmeme.module.comment_vote.CommentVoteRepository;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post.Comment;
import dev.valium.sweetmeme.module.post.CommentRepository;
import org.springframework.context.ApplicationEventPublisher;

public class CommentVoterSelector {
    public static CommentVoter getCommentVoter(Comment comment, Long id, boolean vote,
                                               Member member, CommentVote upVotedComment,
                                               CommentVote downVotedComment,
                                               CommentVoteRepository commentVoteRepository,
                                               CommentRepository commentRepository,
                                               ApplicationEventPublisher eventPublisher) {
        if(downVotedComment == null && upVotedComment == null)
            return FirstVotedCommentVoter.builder()
                    .comment(comment)
                    .id(id)
                    .vote(vote)
                    .member(member)
                    .commentRepository(commentRepository)
                    .commentVoteRepository(commentVoteRepository)
                    .eventPublisher(eventPublisher)
                    .build();
        else if (upVotedComment != null && downVotedComment == null) {
            return UpVotedCommentVoter.builder()
                    .comment(comment)
                    .id(id)
                    .vote(vote)
                    .member(member)
                    .upVotedComment(upVotedComment)
                    .commentRepository(commentRepository)
                    .commentVoteRepository(commentVoteRepository)
                    .eventPublisher(eventPublisher)
                    .build();
        }
        else {
            return DownVotedCommentVoter.builder()
                    .comment(comment)
                    .id(id)
                    .vote(vote)
                    .member(member)
                    .downVotedComment(downVotedComment)
                    .commentRepository(commentRepository)
                    .commentVoteRepository(commentVoteRepository)
                    .eventPublisher(eventPublisher)
                    .build();
        }
    }
}
