package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.domain.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study/{path}")
public class ProfileController {

    @GetMapping("/")
    public String profile(@PathVariable String nickname) {

        // info, memeber,
        return "user/profile";
    }
}
