package dev.valium.sweetmeme.repository;

import dev.valium.sweetmeme.domain.Comment;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.domain.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Slice<Comment> findByPostAndParent(Post post, Comment parent, Pageable pageable);
    Slice<Comment> findByParent(Comment parent, Pageable pageable);
    Comment findCommentById(Long id);
}
