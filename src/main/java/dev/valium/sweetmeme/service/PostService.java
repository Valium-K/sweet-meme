package dev.valium.sweetmeme.service;

import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.domain.PostTag;
import dev.valium.sweetmeme.domain.Tag;
import dev.valium.sweetmeme.repository.PostRepository;
import dev.valium.sweetmeme.repository.PostTagRepository;
import dev.valium.sweetmeme.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j @Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public Post findPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 post를 찾을 수 없습니다."));

        return post;
    }
}
