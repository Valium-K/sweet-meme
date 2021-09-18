package dev.valium.sweetmeme.service;

import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.domain.Vote;
import dev.valium.sweetmeme.repository.PostRepository;
import dev.valium.sweetmeme.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;

    public boolean votePost(Member member, Long id, boolean vote) throws Exception {
        Post post = postRepository.findById(id).orElseThrow(() -> new Exception("post 없음"));

        Vote upVote = voteRepository.findUpVoteByUpVotedMember(member);
        Vote downVote = voteRepository.findDownVoteByDownVotedMember(member);


        System.out.println("-----------");
        System.out.println(upVote);
        System.out.println(downVote);

        // 첫 보트
        if(downVote == null && upVote == null) {
            Vote newVote = new Vote();
            if(vote) {
                newVote.setUpVotedPost(post);
                //post.getUpVotedMember().add(newVote);
                newVote.setUpVotedMember(member);
                //member.getUpVotedPosts().add(newVote);

                post.getVote().addUpVote();
            }
            else {
                newVote.setDownVotedPost(post);
                newVote.setDownVotedMember(member);
                //member.getDownVotedPosts().add(newVote);
                post.getVote().addDownVote();
            }
            voteRepository.save(newVote);
        }
        // upvote한 포스트
        else if(upVote != null && downVote == null) {
            if(vote) {
                voteRepository.delete(upVote);
                post.getVote().subUpvote();
            }
            else {
                voteRepository.delete(upVote);
                post.getVote().subUpvote();
                post.getVote().addDownVote();

                Vote newVote = new Vote();
                newVote.setDownVotedPost(post);
                newVote.setDownVotedMember(member);
                //member.getDownVotedPosts().add(newVote);

                voteRepository.save(newVote);
            }

        }
        // downvote한 포스트
        else {
            if(vote) {
                voteRepository.delete(downVote);
                post.getVote().subDownVote();
                post.getVote().addUpVote();


                Vote newVote = new Vote();
                newVote.setUpVotedPost(post);
                newVote.setUpVotedMember(member);
                //post.getUpVotedMember().add(newVote);
                //member.getUpVotedPosts().add(newVote);

                voteRepository.save(newVote);
            }
            else {
                post.getVote().subDownVote();
                voteRepository.delete(downVote);
            }
        }

        return true;
    }

    public List<Long> findUpVotedPostsId(Member member) {
        List<Vote> upVotes = voteRepository.findByUpVotedMember(member);

        return upVotes.stream().map(Vote::getUpVotedPost).map(Post::getId).collect(Collectors.toList());
    }

    public List<Long> findDownVotedPostsId(Member member) {
        List<Vote> upVotes = voteRepository.findByDownVotedMember(member);

        return upVotes.stream().map(Vote::getDownVotedPost).map(Post::getId).collect(Collectors.toList());
    }
}
