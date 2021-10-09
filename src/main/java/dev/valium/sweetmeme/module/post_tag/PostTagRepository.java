package dev.valium.sweetmeme.module.post_tag;

import dev.valium.sweetmeme.module.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> findPostTagsByPostId(Long id);
    @EntityGraph(attributePaths = {"post"}, type = EntityGraph.EntityGraphType.LOAD)
    List<PostTag> findPostTagsByTagId(Long tagId, Pageable pageable);

    void deleteAllByPost(Post post);
}
