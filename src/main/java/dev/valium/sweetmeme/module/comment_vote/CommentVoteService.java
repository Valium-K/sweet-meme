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

    /**
     * 긁어온 CommentVote의 null값 여부를 이용해 좋아요, 싫어요를 판별 후 presist 한다
     * @param member
     * @param id
     * @param vote
     * @return
     * @throws Exception
     */
    public Member voteComment(Member member, Long id, boolean vote) throws Exception {
        Comment comment = commentRepository.findFetchPostAndInfoById(id).orElseThrow(
                () -> new Exception("CommentVoteService.voteComment(): " + id + "에 해당하는 id가 없습니다.")
        );

        CommentVote upVotedComment = commentVoteRepository.findUpVoteByUpVotedMemberAndUpVotedComment(member, comment);
        CommentVote downVotedComment = commentVoteRepository.findDownVoteByDownVotedMemberAndDownVotedComment(member, comment);

        // TODO 코드가 깔끔하지 못 하다. 디자인 패턴을 적용 할 수 있을까?
        // 첫 vote일 경우
        if(downVotedComment == null && upVotedComment == null) {
            CommentVote commentVote = new CommentVote();

            if(vote) {
                commentVote.setUpVotedComment(comment);
                commentVote.setUpVotedMember(member);

                comment.getVote().addUpVote();

                member.getUpVotedIds().add(id);

                if(!member.getNickname().equals(comment.getCommenter()))
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
        // upvote했던 comment일 경우
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
        // downvote했던 comment일 경우
        else {
            member.getDownVotedIds().remove(id);

            if(vote) {
                commentVoteRepository.delete(downVotedComment);
                comment.getVote().subDownVote();
                comment.getVote().addUpVote();

                CommentVote commentVote = new CommentVote();
                commentVote.setUpVotedComment(comment);
                commentVote.setUpVotedMember(member);

                if(!member.getNickname().equals(comment.getCommenter()))
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
