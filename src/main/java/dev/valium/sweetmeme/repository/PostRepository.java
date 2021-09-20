package dev.valium.sweetmeme.repository;

import dev.valium.sweetmeme.controller.dto.PostViewDto;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join p.postTags t where t.tagId = :id")
    List<Post> findAllByTagId(@Param("id") Long tagId);


    List<Post> findAllByOriginalPosterOrderByCreatedDateDesc(Member op);
}
