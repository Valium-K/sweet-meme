package dev.valium.sweetmeme.repository;

import dev.valium.sweetmeme.domain.Section;
import dev.valium.sweetmeme.domain.enums.SectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findBySectionType(SectionType sectionType);
}
