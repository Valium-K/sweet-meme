package dev.valium.sweetmeme.module.bases;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.section.enums.SectionType;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static dev.valium.sweetmeme.infra.config.FileConfig.DOWNLOAD_URL;
import static dev.valium.sweetmeme.infra.config.FileConfig.FILE_URL;

/**
 * 컨트롤러의 공통기능 클래스
 */
public class BaseController {

    protected void setBaseAttributes(Member member, Model model, String currentMenu) {
        model.addAttribute("member", member);

        List<String> sectionTypes = Arrays.stream(SectionType.values()).map(s->s.name().toLowerCase()).collect(Collectors.toList());
        model.addAttribute("sidebarSectionTypes", sectionTypes);

        model.addAttribute("FILE_URL", FILE_URL);
        model.addAttribute("DOWNLOAD_URL", DOWNLOAD_URL);

        model.addAttribute("currentMenu", currentMenu);
    }
}
