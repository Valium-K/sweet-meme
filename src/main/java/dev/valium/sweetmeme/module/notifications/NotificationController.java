package dev.valium.sweetmeme.module.notifications;

import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @GetMapping("/notifications/peek")
    public String peekNotifications(@CurrentMember Member member, Model model) {

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
        Slice<Notification> notificationSlice = notificationRepository.findNotificationsByMemberIdAndHasRead(member.getId(), false, pageRequest);

        model.addAttribute("notifications", notificationSlice);

        return "fragments :: #notificationPeek";
    }

    @GetMapping("/notification/post/{notificationType}/{postId}")
    public String clickedNotification(@CurrentMember Member member, @PathVariable NotificationType notificationType, @PathVariable Long postId) {

        // TODO SectionType validation

        notificationService.setNotificationHasRead(member.getId(), postId, notificationType);

        return "redirect:/post/" + postId;
    }

    @GetMapping("/notifications/all")
    public String allNotifications() {

        return null;
    }
}
