package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.bases.enums.SectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOriginalPosterOrderByCreatedDateDesc(Member op);

    List<Post> findAllByBelongedSectionTypeOrderByCreatedDateDesc(SectionType type);
}
