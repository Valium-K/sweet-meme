package dev.valium.sweetmeme.repository;

import dev.valium.sweetmeme.domain.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface InfoRepository extends JpaRepository<Info, Long> {

    Info findInfoByHead(String head);
    Optional<Info> findByHead(String head);
}
