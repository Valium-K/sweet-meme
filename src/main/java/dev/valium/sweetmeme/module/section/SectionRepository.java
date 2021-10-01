package dev.valium.sweetmeme.module.section;

import dev.valium.sweetmeme.module.bases.enums.SectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findBySectionType(SectionType sectionType);
}
