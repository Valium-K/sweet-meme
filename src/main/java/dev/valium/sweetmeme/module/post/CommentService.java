package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberRepository;
import dev.valium.sweetmeme.module.member_post.MemberPost;
import dev.valium.sweetmeme.module.member_post.MemberPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberPostRepository memberPostRepository;
    private final PostRepository postRepository;

    public void deleteCommentById(Member member, Long commentId, Long postId, Locale locale) {
        Comment comment = commentRepository.findFetchParentAndPostCommentById(commentId);
        memberPostRepository.deleteByCommentedMemberAndAndCommentedPost(member, comment.getPost());

        if(comment.getReplyCount() == 0) {
            comment.getPost().subCommentCount();
            if(comment.getParent() != null)
                comment.getParent().subReplyCount();
            commentRepository.deleteById(commentId);
        }
        else {
            if("ko".equals(locale.getLanguage())) {
                comment.setContent("해당 내용은 삭제 되었습니다.");
            }
            else {
                comment.setContent("The content has been deleted.");
            }
            comment.setDescriptionImg(null);
            comment.setHasBeenDeleted(true);
        }


    }
}
