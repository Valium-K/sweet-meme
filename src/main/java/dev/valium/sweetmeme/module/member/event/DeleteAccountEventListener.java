package dev.valium.sweetmeme.module.member.event;

import dev.valium.sweetmeme.module.info.Info;
import dev.valium.sweetmeme.module.info.InfoRepository;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberRepository;
import dev.valium.sweetmeme.module.member.MemberService;
import dev.valium.sweetmeme.module.notifications.Notification;
import dev.valium.sweetmeme.module.notifications.NotificationRepository;
import dev.valium.sweetmeme.module.post.Post;
import dev.valium.sweetmeme.module.post.PostRepository;
import dev.valium.sweetmeme.module.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class DeleteAccountEventListener {

    private final PostService postService;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final InfoRepository infoRepository;

    @EventListener
    public void handleDeleteAccountEvent(DeleteAccountEvent deleteAccountEvent) {
        Member member = deleteAccountEvent.getMember();
        List<Post> posts = postRepository.findAllByOriginalPoster(member);

        // TODO in query, batch 최적화 가능
        posts.forEach(p -> {
            postService.deletePost(member, p.getId());
        });

        notificationRepository.deleteAllByMemberId(member.getId());

        Info info = member.getMemberInfo();
        memberRepository.delete(member);

        infoRepository.delete(info);
    }
}
