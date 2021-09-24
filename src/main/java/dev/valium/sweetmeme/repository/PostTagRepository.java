package dev.valium.sweetmeme.repository;

import dev.valium.sweetmeme.domain.PostTag;
import dev.valium.sweetmeme.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> findPostTagsByPostId(Long id);
}
