package dev.valium.sweetmeme.module.post_vote;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberService;
import dev.valium.sweetmeme.module.post.Post;
import dev.valium.sweetmeme.module.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostVoteService {

    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;

    public Member votePost(Member member, Long id, boolean vote) throws Exception {
        Post post = postRepository.findById(id).orElseThrow(() -> new Exception("post 없음"));

        PostVote upPostVote = postVoteRepository.findUpVoteByUpVotedMemberAndUpVotedPost(member, post);
        PostVote downPostVote = postVoteRepository.findDownVoteByDownVotedMemberAndDownVotedPost(member, post);

        // 첫 보트
        if(downPostVote == null && upPostVote == null) {
            PostVote newPostVote = new PostVote();
            if(vote) {
                newPostVote.setUpVotedPost(post);
                newPostVote.setUpVotedMember(member);

                post.getVote().addUpVote();

                member.getUpVotedIds().add(id);
            }
            else {
                newPostVote.setDownVotedPost(post);
                newPostVote.setDownVotedMember(member);

                post.getVote().addDownVote();

                member.getDownVotedIds().add(id);
            }
            postVoteRepository.save(newPostVote);
        }
        // upvote한 포스트
        else if(upPostVote != null && downPostVote == null) {
            if(vote) {
                postVoteRepository.delete(upPostVote);
                post.getVote().subUpvote();

                member.getUpVotedIds().remove(id);
            }
            else {
                postVoteRepository.delete(upPostVote);
                post.getVote().subUpvote();
                post.getVote().addDownVote();

                PostVote newPostVote = new PostVote();
                newPostVote.setDownVotedPost(post);
                newPostVote.setDownVotedMember(member);

                member.getUpVotedIds().remove(id);
                member.getDownVotedIds().add(id);

                postVoteRepository.save(newPostVote);
            }

        }
        // downvote한 포스트
        else {
            if(vote) {
                postVoteRepository.delete(downPostVote);
                post.getVote().subDownVote();
                post.getVote().addUpVote();

                PostVote newPostVote = new PostVote();
                newPostVote.setUpVotedPost(post);
                newPostVote.setUpVotedMember(member);

                member.getDownVotedIds().remove(id);
                member.getUpVotedIds().add(id);

                postVoteRepository.save(newPostVote);
            }
            else {
                post.getVote().subDownVote();

                member.getDownVotedIds().remove(id);

                postVoteRepository.delete(downPostVote);
            }
        }

        return member;
    }

    public List<Long> findUpVotedPostsId(Member member) {
        List<PostVote> upPostVotes = postVoteRepository.findByUpVotedMember(member);

        return upPostVotes.stream().map(PostVote::getUpVotedPost).map(Post::getId).collect(Collectors.toList());
    }

    public List<Long> findDownVotedPostsId(Member member) {
        List<PostVote> upPostVotes = postVoteRepository.findByDownVotedMember(member);

        return upPostVotes.stream().map(PostVote::getDownVotedPost).map(Post::getId).collect(Collectors.toList());
    }

    public List<Post> findUpVotedPosts(Member member) {
        List<PostVote> memberUpPostVotes = postVoteRepository.findAllByUpVotedMember(member);
        List<Post> upVotedPosts = memberUpPostVotes.stream()
                                            .map(PostVote::getUpVotedPost)
                                            .collect(Collectors.toList());

        return upVotedPosts;

    }
}
