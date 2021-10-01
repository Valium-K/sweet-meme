package dev.valium.sweetmeme;

import com.tngtech.archunit.*;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packagesOf = Application.class)
public class PackageDependencyTests {

    private static final String MEMBER = "..module.member..";
    private static final String INFO = "..module.info..";
    private static final String SECTION = "..module.section..";
    private static final String POST = "..module.post..";
    private static final String TAG = "..module.tag..";
    private static final String COMMENT = "..module.comment..";
    private static final String COMMENT_VOTE = "..module.comment_vote..";
    private static final String VOTE = "..module.vote..";
    private static final String POST_TAG = "..module.post_tag..";
    private static final String UPLOAD = "..module.upload..";
    private static final String BASES = "..module.bases..";
    private static final String HOME = "..module.home..";

    @ArchTest
    ArchRule modulesPackageRule = classes().that().resideInAPackage("dev.valium.sweetmeme.module..")
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage("dev.valium.sweetmeme.module..");

    @ArchTest
    ArchRule sectionPackageRule = classes().that().resideInAPackage(SECTION)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(SECTION, POST, UPLOAD);

    @ArchTest
    ArchRule infoPackageRule = classes().that().resideInAPackage(INFO)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(INFO, MEMBER, SECTION, COMMENT, HOME);

    @ArchTest
    ArchRule commentPackageRule = classes().that().resideInAPackage(COMMENT)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(COMMENT, COMMENT_VOTE, POST);

    @ArchTest
    ArchRule memberPackageRule = classes().that().resideInAPackage(MEMBER)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(MEMBER, COMMENT_VOTE, VOTE, POST);

    @ArchTest
    ArchRule postPackageRule = classes().that().resideInAPackage(POST)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(POST,COMMENT, VOTE, POST_TAG, SECTION);

    @ArchTest
    ArchRule comment_votePackageRule = classes().that().resideInAPackage(COMMENT_VOTE)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(COMMENT_VOTE, POST);

    @ArchTest
    ArchRule votePackageRule = classes().that().resideInAPackage(VOTE)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(VOTE, POST, BASES);

    @ArchTest
    ArchRule tagPackageRule = classes().that().resideInAPackage(TAG)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(TAG, POST);

    @ArchTest
    ArchRule homePackageRule = classes().that().resideInAPackage(HOME)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(INFO, POST);

    @ArchTest
    ArchRule cycleCheck = slices().matching("dev.valium.sweetmeme.(*)..")
            .should().beFreeOfCycles();
}
