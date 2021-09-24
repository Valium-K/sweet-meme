package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Vote;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static dev.valium.sweetmeme.config.FileConfig.DOWNLOAD_URL;
import static dev.valium.sweetmeme.config.FileConfig.FILE_URL;


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
