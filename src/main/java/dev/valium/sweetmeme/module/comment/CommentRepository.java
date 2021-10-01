package dev.valium.sweetmeme.module.comment;

import dev.valium.sweetmeme.module.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Slice<Comment> findByPostAndParent(Post post, Comment parent, Pageable pageable);
    Slice<Comment> findByParent(Comment parent, Pageable pageable);
    Comment findCommentById(Long id);
}
