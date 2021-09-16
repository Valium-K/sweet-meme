package dev.valium.sweetmeme.repository;

import dev.valium.sweetmeme.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberFetchRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m join fetch m.memberInfo where m.id = :id")
    Optional<Member> findFetchInfoById(@Param("id") Long id);
}
