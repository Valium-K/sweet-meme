package dev.valium.sweetmeme.module.home;

import dev.valium.sweetmeme.module.bases.BaseController;
import dev.valium.sweetmeme.module.bases.enums.SectionType;
import dev.valium.sweetmeme.module.info.Info;
import dev.valium.sweetmeme.module.member.CurrentMember;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.post.Post;
import dev.valium.sweetmeme.module.info.InfoRepository;
import dev.valium.sweetmeme.module.post.PostRepository;
import dev.valium.sweetmeme.module.vote.VoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HomeController extends BaseController {

    private final PostRepository postRepository;
    private final InfoRepository infoRepository;

    public HomeController(VoteService voteService, PostRepository postRepository, InfoRepository infoRepository) {
        super(voteService);
        this.postRepository = postRepository;
        this.infoRepository = infoRepository;
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
