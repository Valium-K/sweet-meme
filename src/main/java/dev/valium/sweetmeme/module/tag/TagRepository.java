package dev.valium.sweetmeme.module.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByTagName(String name);

    @Query("select t from Tag t where t in :tags")
    List<Tag> findByTagIds(@Param("tags") List<Tag> tags);
}
