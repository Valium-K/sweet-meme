package dev.valium.sweetmeme.service;

import dev.valium.sweetmeme.config.FileConfig;
import dev.valium.sweetmeme.controller.dto.CommentForm;
import dev.valium.sweetmeme.domain.*;
import dev.valium.sweetmeme.processor.FileProcessor;
import dev.valium.sweetmeme.repository.CommentRepository;
import dev.valium.sweetmeme.repository.CommentVoteRepository;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentVoteRepository commentVoteRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberService memberService;

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

    public void saveComment(Long postId, CommentForm form, Member member) throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException(postId + "id의 포스트가 없음"));
        Member foundMember = memberService.findMemberAndInfo(member.getNickname());
        Comment comment = Comment.create(foundMember.getMemberInfo());

        if(!foundMember.getMyPosts().contains(post)) {
            foundMember.getCommentedPosts().add(post);
        }

        if(!form.getFile().isEmpty()) {
            String fileName = FileProcessor.transferFile(FileConfig.ABSOLUTE_COMMENT_IMAGE_PATH, form.getFile(), true);
            comment.setDescriptionImg(fileName);
        }

        comment.setContent(form.getContent());
        post.addCommentCount();
        comment.setPost(post);
        post.getComments().add(comment);

        commentRepository.save(comment);
    }
}
