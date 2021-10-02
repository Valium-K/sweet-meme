package dev.valium.sweetmeme.module.home;

import dev.valium.sweetmeme.module.bases.BaseController;
import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post_vote.PostVoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController extends BaseController {

    public HomeController(PostVoteService postVoteService) {
        super(postVoteService);
    }


    @GetMapping
    public String home(@CurrentMember Member member, Model model) {
        setBaseAttributes(member, model, "hot");

        return "home/home";
    }

    @GetMapping("/login")
    public String loginForm(@CurrentMember Member member, HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);

        if(member == null)
            return "login";
        else
            return "redirect:/";
    }
}
