package dev.valium.sweetmeme.config;

import dev.valium.sweetmeme.domain.Info;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Section;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.service.MemberService;
import dev.valium.sweetmeme.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.*;
import java.nio.file.Files;
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
        private final UploadService uploadService;
        private final MemberRepository memberRepository;

        private final String NICKNAME = "testtest";
        private final String EMAIL = "test@test.test";
        private final String PASSWORD = "testtest";
        private final String PICURL = null;
        private final String DESCRIPTION = "testdescription";

        public void initDB() {
            memberInit();
            sectionInit();
            uploadInit();
        }

        private void uploadInit() {
            String tags = "[{value : tag1}, {value : tag2}, {value : tag3}]";
            String section = "[{value : FUNNY}]";

            try {
                File file = new File("D:/sweetmeme/test/images/file.jpg");
                FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()),
                        false, file.getName(), (int) file.length(), file.getParentFile());

                IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());

                MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

                Member member = memberRepository.findMemberByNickname("testtest");
                uploadService.uploadPost(member, "Title for test", tags, section, multipartFile);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void memberInit() {
            Info info = Info.createInfo(PICURL, NICKNAME, DESCRIPTION);
            info.setStateCode("kr");
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
}
