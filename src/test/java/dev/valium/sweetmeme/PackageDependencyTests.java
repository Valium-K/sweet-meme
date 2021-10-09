package dev.valium.sweetmeme;

import com.tngtech.archunit.*;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import dev.valium.sweetmeme.module.post_tag.PostTag;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packagesOf = Application.class)
public class PackageDependencyTests {

    private static final String MEMBER = "..module.member..";
    private static final String INFO = "..module.info..";
    private static final String SECTION = "..module.section..";
    private static final String POST = "..module.post..";
    private static final String TAG = "..module.tag..";
    private static final String COMMENT_VOTE = "..module.comment_vote..";
    private static final String POST_VOTE = "..module.post_vote..";
    private static final String POST_TAG = "..module.post_tag..";
    private static final String BASES = "..module.bases..";
    private static final String HOME = "..module.home..";
    private static final String MEMBER_POST = "..module.member_post..";
    private static final String NOTIFICATIONS = "..module.notifications..";

    @ArchTest
    ArchRule modulesPackageRule = classes().that().resideInAPackage("dev.valium.sweetmeme.module..")
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage("dev.valium.sweetmeme.module..");

    @ArchTest
    ArchRule sectionPackageRule = classes().that().resideInAPackage(SECTION)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(SECTION, POST, POST_VOTE); // POST, POST_VOTE는 sectioninit떄문에 어쩔수 없이 넣음. 나중에 수정할 것

    @ArchTest
    ArchRule infoPackageRule = classes().that().resideInAPackage(INFO)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(INFO, MEMBER, SECTION, HOME, POST);
    @ArchTest
    ArchRule memberPackageRule = classes().that().resideInAPackage(MEMBER)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(MEMBER, MEMBER_POST, POST_VOTE, COMMENT_VOTE, POST, HOME, NOTIFICATIONS);

    @ArchTest
    ArchRule postPackageRule = classes().that().resideInAPackage(POST)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(POST, MEMBER_POST, COMMENT_VOTE, POST_VOTE, SECTION, NOTIFICATIONS);

    @ArchTest
    ArchRule comment_votePackageRule = classes().that().resideInAPackage(COMMENT_VOTE)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(COMMENT_VOTE, POST);

    @ArchTest
    ArchRule post_votePackageRule = classes().that().resideInAPackage(POST_VOTE)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(POST_VOTE, POST, BASES, MEMBER);

    @ArchTest
    ArchRule tagPackageRule = classes().that().resideInAPackage(TAG)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(TAG, POST_TAG, POST, SECTION);

    @ArchTest
    ArchRule homePackageRule = classes().that().resideInAPackage(HOME)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(HOME, POST);

    @ArchTest
    ArchRule cycleCheck = slices().matching("dev.valium.sweetmeme.(*)..")
            .should().beFreeOfCycles();
}
