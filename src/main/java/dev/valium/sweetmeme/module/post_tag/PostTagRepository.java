package dev.valium.sweetmeme.module.post_tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> findPostTagsByPostId(Long id);
}
