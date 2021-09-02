package dev.valium.sweetmeme.repository;

import dev.valium.sweetmeme.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByNickname(String username);

    Member findByEmail(String email);
}
