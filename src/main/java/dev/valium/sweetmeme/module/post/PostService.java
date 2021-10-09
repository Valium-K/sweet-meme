package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.infra.config.FileConfig;
import dev.valium.sweetmeme.module.comment_vote.CommentVoteRepository;
import dev.valium.sweetmeme.module.member_post.MemberPost;
import dev.valium.sweetmeme.module.member_post.MemberPostRepository;
import dev.valium.sweetmeme.module.post_vote.PostVote;
import dev.valium.sweetmeme.module.post_vote.PostVoteRepository;
import dev.valium.sweetmeme.module.processor.FileProcessor;
import dev.valium.sweetmeme.module.notifications.NotificationType;
import dev.valium.sweetmeme.module.post.form.CommentForm;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member_post.MemberPostService;
import dev.valium.sweetmeme.module.post_tag.PostTag;
import dev.valium.sweetmeme.module.post_tag.PostTagRepository;
import dev.valium.sweetmeme.module.notifications.event.NotificationEvent;
import dev.valium.sweetmeme.module.tag.Tag;
import dev.valium.sweetmeme.module.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j @Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final int DELETE_COMMENT_BATCH_SIZE = 50;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final MemberPostService memberPostService;
    private final ApplicationEventPublisher eventPublisher;
    private final PostVoteRepository postVoteRepository;
    private final MemberPostRepository memberPostRepository;
    private final CommentVoteRepository commentVoteRepository;

    // TODO Excpetion 구현
    public Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(id + "에 해당하는 post를 찾을 수 없습니다.")
        );
    }

    public void saveComment(Long postId, CommentForm form, Member member) throws IOException {
        Comment comment = setComment(postId, form, member);

        commentRepository.save(comment);
    }

    public void saveReply(Long postId, Long parentCommentId, CommentForm form, Member member) throws IOException {
        Comment comment = setComment(postId, form, member);
        Comment parentComment = commentRepository.findCommentById(parentCommentId);

        comment.setParent(parentComment);
        parentComment.getChildren().add(comment);
        parentComment.addReplyCount();

        commentRepository.save(comment);
    }

    private Comment setComment(Long postId, CommentForm form, Member member) throws IOException {
        Post post = postRepository.findFetchOPById(postId).orElseThrow(() -> new IllegalArgumentException(postId + "id의 포스트가 없음"));

        Comment comment = Comment.create(member.getMemberInfo());

        List<Post> commentedPosts = memberPostService.findPostsByMember(member);
        if(!commentedPosts.contains(post)) {
            memberPostService.addCommentedPost(member, post);
        }

        if(!member.getNickname().equals(post.getOriginalPoster().getNickname()))
            eventPublisher.publishEvent(new NotificationEvent(post, member, NotificationType.COMMENT));

        if(form.getFile() != null && !form.getFile().isEmpty()) {
            String fileName = FileProcessor.transferFile(FileConfig.ABSOLUTE_COMMENT_IMAGE_PATH, form.getFile(), true);
            comment.setDescriptionImg(fileName);
        }

        comment.setContent(form.getContent());
        post.addCommentCount();
        comment.setPost(post);
        post.getComments().add(comment);

        return comment;
    }

    public List<Tag> findTags(Post post) {

        List<Long> tagIds = postTagRepository.findPostTagsByPostId(post.getId()).stream()
                .map(PostTag::getTagId)
                .collect(Collectors.toList());

        List<Tag> tags = tagRepository.findByTagIds(tagIds);

        return tags;
    }

    public List<Post> findFetchPostsByCommentedMember(Member member) {
        return memberPostService.findFetchPostsByCommentedMember(member);
    }

    public void deletePost(Member member, Long postId) {

        Post post = postRepository.findFetchOPById(postId).orElseThrow(() -> {
            log.error("PostService.deletePost(): " + postId + "에 해당하는 post가 없습니다.");
            return new IllegalArgumentException();
        });

        if(!post.getOriginalPoster().getId().equals(member.getId())) {
            return;
        }
        postVoteRepository.deleteAllByUpVotedPost(post);
        postVoteRepository.deleteAllByDownVotedPost(post);

        postTagRepository.deleteAllByPost(post);

        memberPostRepository.deleteAllByCommentedPost(post);

        for(int i = 0;true;i+=DELETE_COMMENT_BATCH_SIZE) {
            Pageable pageRequest = PageRequest.of(i, DELETE_COMMENT_BATCH_SIZE, Sort.by("createdDate"));
            Slice<Comment> comments = commentRepository.findAllByPost(post, pageRequest);

            commentVoteRepository.deleteAllByDownVotedCommentIn(comments.toList());
            commentVoteRepository.deleteAllByUpVotedCommentIn(comments.toList());

            if(!comments.hasNext())
                break;
        }

        commentRepository.deleteAllByPost(post);

        postRepository.delete(post);
    }
}
