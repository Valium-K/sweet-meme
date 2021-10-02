package dev.valium.sweetmeme.module.info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface InfoRepository extends JpaRepository<Info, Long> {

    Info findInfoByHead(String head);
}
