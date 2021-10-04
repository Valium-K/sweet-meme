//package dev.valium.sweetmeme.infra.config;
//
//import dev.valium.sweetmeme.module.bases.enums.SectionType;
//import dev.valium.sweetmeme.module.info.Info;
//import dev.valium.sweetmeme.module.member.MemberRepository;
//import dev.valium.sweetmeme.module.post.CommentRepository;
//import dev.valium.sweetmeme.module.post.PostRepository;
//import dev.valium.sweetmeme.module.post.UploadService;
//import dev.valium.sweetmeme.module.section.Section;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import javax.persistence.EntityManager;
//import java.util.Arrays;
//import java.util.Locale;
//
//@Profile("dev")
//@Component
//@RequiredArgsConstructor
//public class LocalDbInit {
//
//    private final InitService initService;
//
//    @PostConstruct
//    public void init() {
//        initService.initDB();
//    }
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService {
//
//        /**
//         * nickname: test
//         * email: test@test.test
//         * password: testtest
//         */
//        private final EntityManager entityManager;
//        private final PasswordEncoder passwordEncoder;
//        private final MessageSource messageSource;
//        private final UploadService uploadService;
//        private final MemberRepository memberRepository;
//        private final PostRepository postRepository;
//        private final CommentRepository commentRepository;
//
//        private final String NICKNAME = "testtest";
//        private final String EMAIL = "test@test.test";
//        private final String PASSWORD = "testtest";
//        private final String PICURL = null;
//        private final String DESCRIPTION = "testdescription";
//
//        public void initDB() {
//            try {
////                memberInit();
//                sectionInit();
////                uploadInit();
////                commentInit(21);
////                replyInit(21);
//            }
//            catch (Exception e) {
//                e.getStackTrace();
//            }
//        }
//
////        private void replyInit(int numberOfReply) throws IOException {
////            Member member = memberRepository.findMemberAndInfoByNickname(NICKNAME).get();
////            CommentForm commentForm = new CommentForm();
////            Post post = postRepository.findAll().get(0);
////            commentForm.setFile(null);
////
////            Comment comment = commentRepository.findAll().get(0);
////            for(int i = 1; i <= numberOfReply; i++) {
////                commentForm.setContent(i + "번째 리플.");
////
////                commentService.saveReply(post.getId(), comment.getId(), commentForm, member);
////            }
////        }
////
////        private void commentInit(int numberOfComment) throws IOException {
////            Member member = memberRepository.findMemberAndInfoByNickname(NICKNAME).get();
////            CommentForm commentForm = new CommentForm();
////            Post post = postRepository.findAll().get(0);
////            commentForm.setFile(null);
////
////            for(int i = 1; i <= numberOfComment; i++) {
////                commentForm.setContent(i + "번째 코맨트.");
////
////                commentService.saveComment(post.getId(), commentForm, member);
////            }
////
////
////        }
////
////        private void uploadInit() {
////            String tags = "[{value : tag1}, {value : tag2}, {value : tag3}]";
////            String section = "[{value : FUNNY}]";
////
////            try {
////                File file = new File("D:/sweetmeme/test/images/file.jpg");
////                FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()),
////                        false, file.getName(), (int) file.length(), file.getParentFile());
////
////                IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
////
////                MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
////
////                Member member = memberRepository.findMemberByNickname("testtest");
////                uploadService.uploadPost(member, "Title for test", tags, section, multipartFile);
////
////            } catch (Exception ex) {
////                ex.printStackTrace();
////            }
////        }
////
////        private void memberInit() {
////            Info info = Info.createInfo(PICURL, NICKNAME, DESCRIPTION);
////            info.setStateCode("kr");
////            Member member = Member.createMember(NICKNAME, EMAIL, passwordEncoder.encode(PASSWORD));
////            info.setHead(member.getNickname());
////            member.setMemberInfo(info);
////
////            entityManager.persist(member);
////        }
//        private void sectionInit() {
//            Arrays.asList(SectionType.values()).forEach(sectionType -> {
//                Section section = Section.createSection(
//                        sectionType
//                        , Info.createInfo(
//                                sectionType.name().toLowerCase(),
//                                sectionType.name().toUpperCase(),
//                                messageSource.getMessage("section." + sectionType.name().toLowerCase(Locale.US) + ".description"
//                                                         , new Object[0]
//                                                         , Locale.US)
//                        ));
//                entityManager.persist(section);
//            });
//        }
//    }
//}
