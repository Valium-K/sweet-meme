package dev.valium.sweetmeme.module.member_post;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberPostService {

    private final MemberPostRepository memberPostRepository;

    public void addCommentedPost(Member member, Post post) {
        MemberPost memberPost = MemberPost.create(member, post);

        memberPostRepository.save(memberPost);
    }

    public List<Post> findPostsByMember(Member member) {
        return memberPostRepository.findByCommentedMember(member)
                .stream()
                .map(MemberPost::getCommentedPost)
                .collect(Collectors.toList());
    }

    public List<Post> findFetchPostsByCommentedMember(Member member) {
        return memberPostRepository.findFetchPostByCommentedMember(member)
                .stream()
                .map(MemberPost::getCommentedPost)
                .collect(Collectors.toList());
    }
}
