package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.infra.config.FileConfig;
import dev.valium.sweetmeme.module.comment_vote.CommentVoteRepository;
import dev.valium.sweetmeme.module.member_post.MemberPost;
import dev.valium.sweetmeme.module.member_post.MemberPostRepository;
import dev.valium.sweetmeme.module.post.exceptions.NoSuchPostException;
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

    public Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new NoSuchPostException(id)
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
        Post post = postRepository.findFetchOPById(postId).orElseThrow(() -> new NoSuchPostException(postId));

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

        List<Tag> tags = postTagRepository.findPostTagsByPostId(post.getId()).stream()
                .map(PostTag::getTag)
                .collect(Collectors.toList());

        // List<Tag> tagss = tagRepository.findByTagIds(tags);

        return tags;
    }

    public List<Post> findFetchPostsByCommentedMember(Member member) {
        return memberPostService.findFetchPostsByCommentedMember(member);
    }

    public void deletePost(Member member, Long postId) {

        Post post = postRepository.findFetchOPById(postId).orElseThrow(() -> {
            // log.error("PostService.deletePost(): " + postId + "에 해당하는 post가 없습니다.");
            return new NoSuchPostException(postId);
        });

        if(!post.getOriginalPoster().getId().equals(member.getId())) {
            return;
        }
        // 1. postVote 삭제
        postVoteRepository.deleteAllByUpVotedPost(post);
        postVoteRepository.deleteAllByDownVotedPost(post);

        // 2. tag 삭제
        postTagRepository.deleteAllByPost(post);

        // 3. memberPost 삭제
        memberPostRepository.deleteAllByCommentedPost(post);

        // 4. commentVote의 삭제
        // Post의 comment를 DELETE_COMMENT_BATCH_SIZE만큼 가져와 delete한다.
        for(int i = 0; true; i += DELETE_COMMENT_BATCH_SIZE) {
            Pageable pageRequest = PageRequest.of(i, DELETE_COMMENT_BATCH_SIZE, Sort.by("createdDate"));
            Slice<Comment> comments = commentRepository.findAllByPost(post, pageRequest);

            commentVoteRepository.deleteAllByDownVotedCommentIn(comments.toList());
            commentVoteRepository.deleteAllByUpVotedCommentIn(comments.toList());

            if(!comments.hasNext())
                break;
        }

        // 5. comment 삭제
        commentRepository.deleteAllByPost(post);

        // 6. post 삭제
        postRepository.delete(post);
    }
}
