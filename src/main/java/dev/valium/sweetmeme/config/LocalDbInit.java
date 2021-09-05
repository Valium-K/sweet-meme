package dev.valium.sweetmeme.config;

import dev.valium.sweetmeme.domain.Info;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

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
        private final MemberService memberService;

        private final String NICKNAME = "test";
        private final String EMAIL = "test@test.test";
        private final String PASSWORD = "testtest";
        private final String PICURL = null;
        private final String DESCRIPTION = "test description";

        public void initDB() {
            Info info = Info.createInfo(PICURL, DESCRIPTION);
            Member member = Member.createMember(NICKNAME, EMAIL, passwordEncoder.encode(PASSWORD));
            info.setHead(member.getNickname());
            member.setMemberInfo(info);

            entityManager.persist(member);
        }
    }
}
