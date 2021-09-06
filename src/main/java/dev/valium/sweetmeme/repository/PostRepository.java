package dev.valium.sweetmeme.repository;

import dev.valium.sweetmeme.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
