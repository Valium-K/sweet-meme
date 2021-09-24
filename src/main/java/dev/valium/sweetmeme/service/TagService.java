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
public class TagService {

    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    public List<Tag> findTags(Post post) {

        List<Long> tagIds = postTagRepository.findPostTagsByPostId(post.getId()).stream()
                .map(PostTag::getTagId)
                .collect(Collectors.toList());

        List<Tag> tags = tagRepository.findByTagIds(tagIds);

        return tags;
    }
}
