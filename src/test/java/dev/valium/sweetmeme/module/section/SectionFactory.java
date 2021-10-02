package dev.valium.sweetmeme.module.section;

import dev.valium.sweetmeme.module.bases.enums.SectionType;
import dev.valium.sweetmeme.module.info.Info;
import dev.valium.sweetmeme.module.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.Arrays;
import java.util.Locale;

@Component
public class SectionFactory {
    @Autowired private MessageSource messageSource;
    @Autowired private EntityManager entityManager;

    public void init() {
        Arrays.asList(SectionType.values()).forEach(sectionType -> {
            Section section = Section.createSection(
                    sectionType
                    , Info.createInfo(
                            sectionType.name().toLowerCase(),
                            sectionType.name().toUpperCase(),
                            messageSource.getMessage("section." + sectionType.name().toLowerCase(Locale.US) + ".description"
                                    , new Object[0]
                                    , Locale.US)
                    ));
            entityManager.persist(section);
        });
    }
}
