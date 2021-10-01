package dev.valium.sweetmeme.module.bases;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.bases.enums.SectionType;
import dev.valium.sweetmeme.module.vote.VoteService;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static dev.valium.sweetmeme.infra.config.FileConfig.DOWNLOAD_URL;
import static dev.valium.sweetmeme.infra.config.FileConfig.FILE_URL;


public class BaseController {

    private final VoteService voteService;

    public BaseController(VoteService voteService) {
        this.voteService = voteService;
    }

    protected void setBaseAttributes(Member member, Model model, String currentMenu) {
        if(member == null) {
            model.addAttribute("member", null);
        } else {
            model.addAttribute("member", member);

            List<Long> upVoteIds = voteService.findUpVotedPostsId(member);
            model.addAttribute("upVotedIds", upVoteIds);
            List<Long> downVoteIds = voteService.findDownVotedPostsId(member);
            model.addAttribute("downVotedIds", downVoteIds);
        }

        List<String> sectionTypes = Arrays.stream(SectionType.values()).map(s->s.name().toLowerCase()).collect(Collectors.toList());
        model.addAttribute("sidebarSectionTypes", sectionTypes);

        model.addAttribute("FILE_URL", FILE_URL);
        model.addAttribute("DOWNLOAD_URL", DOWNLOAD_URL);

        model.addAttribute("currentMenu", currentMenu);
    }
}
