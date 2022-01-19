package dev.valium.sweetmeme.module.home;

import dev.valium.sweetmeme.module.bases.BaseController;
import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post_vote.PostVoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController extends BaseController {

    @GetMapping("/login")
    public String loginForm(@CurrentMember Member member, Model model, HttpServletRequest request) {
        setBaseAttributes(member, model, "login");
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);

        if(member == null)
            return "login";
        else
            return "redirect:/";
    }

    @RequestMapping("/robots.txt")
    @ResponseBody
    public String robots(){
        return "User-agent: *\n" +
                "Disallow: /";
    }
}
