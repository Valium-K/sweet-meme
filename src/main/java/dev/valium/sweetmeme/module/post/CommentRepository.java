package dev.valium.sweetmeme.module.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"commenterInfo"}, type = EntityGraph.EntityGraphType.LOAD)
    Slice<Comment> findByPostIdAndParent(Long id, Comment parent, Pageable pageable);
    @EntityGraph(attributePaths = {"commenterInfo"}, type = EntityGraph.EntityGraphType.LOAD)
    Slice<Comment> findByParentId(Long parent, Pageable pageable);
    Comment findCommentById(Long id);
}
