package dev.valium.sweetmeme.module.notifications;

import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@AllArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @GetMapping("/notifications/peek")
    public String peekNotifications(@CurrentMember Member member, Model model) {

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
        Slice<Notification> notificationSlice = notificationRepository.findNotificationsByMemberId(member.getId(), pageRequest);

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
    public String allNotifications(@CurrentMember Member member, Model model) {

        List<Notification> notifications = notificationRepository.findByMemberId(member.getId());
        model.addAttribute("notifications", notifications);
        model.addAttribute(member);

        return "notifications";
    }

    @PostMapping("/notifications/set/all-has-read")
    public ResponseEntity setAllHasRead(@CurrentMember Member member, Model model) {

        List<Notification> notifications = notificationService.setAllHasRead(member);

        model.addAttribute("successToSetAllHasRead", true);
        model.addAttribute("notifications", notifications);
        model.addAttribute(member);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications/remove/all-has-read")
    public ResponseEntity removeAllHasRead(@CurrentMember Member member, Model model) {

        boolean success = notificationService.removeAllHasRead(member);

        return ResponseEntity.ok().build();
    }
}
