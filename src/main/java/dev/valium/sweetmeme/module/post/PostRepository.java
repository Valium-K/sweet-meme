package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.module.bases.enums.SectionType;
import dev.valium.sweetmeme.module.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOriginalPosterOrderByCreatedDateDesc(Member op);

    List<Post> findAllByBelongedSectionType(SectionType type, Pageable pageable);

    @EntityGraph(attributePaths = {"originalPoster"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Post> findFetchOPById(Long id);

    Slice<Post> findAllByCreatedDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Post> findAllByOriginalPoster(Member member);
}
