package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.module.info.Info;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"commenterInfo"}, type = EntityGraph.EntityGraphType.LOAD)
    Slice<Comment> findByPostIdAndParent(Long id, Comment parent, Pageable pageable);
    @EntityGraph(attributePaths = {"commenterInfo"}, type = EntityGraph.EntityGraphType.LOAD)
    Slice<Comment> findByParentId(Long parent, Pageable pageable);
    @EntityGraph(attributePaths = {"parent"}, type = EntityGraph.EntityGraphType.LOAD)
    Comment findCommentById(Long id);
    @EntityGraph(attributePaths = {"parent", "post"}, type = EntityGraph.EntityGraphType.LOAD)
    Comment findFetchParentAndPostCommentById(Long id);
    @EntityGraph(attributePaths = {"commenterInfo"}, type = EntityGraph.EntityGraphType.LOAD)
    Slice<Comment> findAllByPost(Post post, Pageable pageable);
    @EntityGraph(attributePaths = {"post", "commenterInfo"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Comment> findFetchPostAndInfoById(Long id);
    List<Comment> findAllByCommenterInfoAndHasBeenDeleted(Info info, boolean hasBeenDeleted);
    void deleteAllByPost(Post post);
}
