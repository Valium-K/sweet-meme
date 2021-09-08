package dev.valium.sweetmeme.config;

import dev.valium.sweetmeme.domain.Info;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Section;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Profile("local")
@Component
@RequiredArgsConstructor
public class LocalDbInit {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initDB();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        /**
         * nickname: test
         * email: test@test.test
         * password: testtest
         */
        private final EntityManager entityManager;
        private final PasswordEncoder passwordEncoder;
        private final MessageSource messageSource;

        private final String NICKNAME = "test";
        private final String EMAIL = "test@test.test";
        private final String PASSWORD = "testtest";
        private final String PICURL = null;
        private final String DESCRIPTION = "test description";

        public void initDB() {
            memberInit();
            sectionInit();

        }

        private void memberInit() {
            Info info = Info.createInfo(PICURL, NICKNAME, DESCRIPTION);
            Member member = Member.createMember(NICKNAME, EMAIL, passwordEncoder.encode(PASSWORD));
            info.setHead(member.getNickname());
            member.setMemberInfo(info);

            entityManager.persist(member);
        }
        private void sectionInit() {
            List<SectionType> sectionTypes = Arrays.asList(SectionType.values());

            Arrays.asList(SectionType.values()).forEach(sectionType -> {
                Section section = Section.createSection(
                        sectionType
                        , Info.createInfo(
                                null,
                                sectionType.name(),
                                messageSource.getMessage("section." + sectionType.name().toLowerCase(Locale.US) + ".description"
                                                         , new Object[0]
                                                         , Locale.US)
                        ));
                entityManager.persist(section);
            });
        }
    }
}
