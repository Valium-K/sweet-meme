package dev.valium.sweetmeme.module.member;

import dev.valium.sweetmeme.module.bases.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MemberController extends BaseController {

    @GetMapping("/login/forgot")
    public String forgetPassword(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "forgot");

        return "forgotPassword";
    }
}
