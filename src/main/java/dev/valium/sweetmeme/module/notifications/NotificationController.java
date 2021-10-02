package dev.valium.sweetmeme.module.notifications;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationController {

    @GetMapping("/notifications/peek")
    public String peekNotifications() {
        return null;
    }
    @GetMapping("/notifications/all")
    public String allNotifications() {

        return null;
    }
}
