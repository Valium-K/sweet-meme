package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.bases.enums.SectionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOriginalPosterOrderByCreatedDateDesc(Member op);

    List<Post> findAllByBelongedSectionTypeOrderByCreatedDateDesc(SectionType type);

    @EntityGraph(attributePaths = {"originalPoster"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Post> findFetchOPById(Long id);

    // Slice<Post> findAll(Pageable pageable);
}
