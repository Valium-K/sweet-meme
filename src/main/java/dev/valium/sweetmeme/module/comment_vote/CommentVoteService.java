package dev.valium.sweetmeme.module.comment_vote;

import dev.valium.sweetmeme.module.comment_vote.commentvoter.CommentVoter;
import dev.valium.sweetmeme.module.comment_vote.commentvoter.CommentVoterSelector;
import dev.valium.sweetmeme.module.comment_vote.commentvoter.FirstVotedCommentVoter;
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

        CommentVoter cVoter = getCommentVoter(comment, id, vote, member, upVotedComment, downVotedComment, commentVoteRepository, commentRepository, eventPublisher);
        cVoter.voteComment();

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

    private CommentVoter getCommentVoter(Comment comment, Long id, boolean vote,
                                         Member member, CommentVote upVotedComment,
                                         CommentVote downVotedComment,
                                         CommentVoteRepository commentVoteRepository,
                                         CommentRepository commentRepository,
                                         ApplicationEventPublisher eventPublisher) {

        return CommentVoterSelector.getCommentVoter(comment, id, vote, member, upVotedComment, downVotedComment, commentVoteRepository, commentRepository, eventPublisher);
    }
}
