package dev.valium.sweetmeme.module.comment_vote;

import dev.valium.sweetmeme.module.notifications.NotificationType;
import dev.valium.sweetmeme.module.post.Comment;
import dev.valium.sweetmeme.module.post.CommentRepository;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.notifications.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    public Member voteComment(Member member, Long id, boolean vote) throws Exception {
        // TODO Exception 구현
        Comment comment = commentRepository.findFetchPostAndInfoById(id).orElseThrow(() -> new Exception("comment 없음"));

        CommentVote upVotedComment = commentVoteRepository.findUpVoteByUpVotedMemberAndUpVotedComment(member, comment);
        CommentVote downVotedComment = commentVoteRepository.findDownVoteByDownVotedMemberAndDownVotedComment(member, comment);

        // 첫 보트
        if(downVotedComment == null && upVotedComment == null) {
            CommentVote commentVote = new CommentVote();

            if(vote) {
                commentVote.setUpVotedComment(comment);
                commentVote.setUpVotedMember(member);

                comment.getVote().addUpVote();

                member.getUpVotedIds().add(id);

                if(!member.getNickname().equals(comment.getCommenterInfo().getHead()))
                    eventPublisher.publishEvent(new NotificationEvent(comment.getPost(), member, NotificationType.UPVOTE_COMMENT));
            }
            else {
                commentVote.setDownVotedComment(comment);
                commentVote.setDownVotedMember(member);

                comment.getVote().addDownVote();

                member.getDownVotedIds().add(id);
            }
            commentVoteRepository.save(commentVote);
        }
        // upvote한 comment
        else if(upVotedComment != null && downVotedComment == null) {

            member.getUpVotedIds().remove(id);

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

                member.getDownVotedIds().add(id);

                commentVoteRepository.save(commentVote);
            }
        }
        // downvote한 comment
        else {
            member.getDownVotedIds().remove(id);

            if(vote) {
                commentVoteRepository.delete(downVotedComment);
                comment.getVote().subDownVote();
                comment.getVote().addUpVote();

                CommentVote commentVote = new CommentVote();
                commentVote.setUpVotedComment(comment);
                commentVote.setUpVotedMember(member);

                if(!member.getNickname().equals(comment.getCommenterInfo().getHead()))
                    eventPublisher.publishEvent(new NotificationEvent(comment.getPost(), member, NotificationType.UPVOTE_COMMENT));

                commentVoteRepository.save(commentVote);
            }
            else {
                comment.getVote().subDownVote();

                member.getUpVotedIds().add(id);
                commentVoteRepository.delete(downVotedComment);
            }
        }

        return member;
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
