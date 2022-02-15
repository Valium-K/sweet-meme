package dev.valium.sweetmeme.module.post;

import dev.valium.sweetmeme.infra.config.FileConfig;
import dev.valium.sweetmeme.module.section.enums.SectionType;
import dev.valium.sweetmeme.module.processor.FileProcessor;
import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberRepository;
import dev.valium.sweetmeme.module.post_tag.PostTag;
import dev.valium.sweetmeme.module.post_tag.PostTagRepository;
import dev.valium.sweetmeme.module.section.Section;
import dev.valium.sweetmeme.module.section.SectionRepository;
import dev.valium.sweetmeme.module.tag.Tag;
import dev.valium.sweetmeme.module.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {
    private final String ABSOLUTE_UPLOAD_PATH = FileConfig.ABSOLUTE_UPLOAD_PATH;

    private final MemberRepository memberRepository;
    private final SectionRepository sectionRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;


    public void uploadPost(Member member, String title, String jsonTags, String jsonSectionType, MultipartFile file) throws Exception {

        // 파일전송
        String fileName = FileProcessor.transferFile(ABSOLUTE_UPLOAD_PATH, file, true);

        // post - sectionType 설정
        SectionType sectionType = json2SectionTypeList(jsonSectionType).get(0);
        Post post = Post.createPost(title, sectionType);

        // post - 파일 url 설정
        post.setPostImageUrl(fileName);

        // post - section 설정
        Section section = sectionRepository.findBySectionType(sectionType);
        post.setSection(section);
        section.getPosts().add(post);

        // post - postTag 설정
        postTagSettingsProcess(post, jsonTags);

        // member - post 설정
        Member foundMember = memberRepository.findMemberById(member.getId());
        post.setOriginalPoster(foundMember);
        foundMember.getMyPosts().add(post);

        // file 전송
        // file.transferTo(newFile);
    }

    private void postTagSettingsProcess(Post post, String jsonTags) throws Exception {
        // post -> tag 태그 생성 및 관계설정
        if(jsonTags != null && !"".equals(jsonTags)) {
            Set<Tag> tags = json2TagSet(jsonTags);

            tags.forEach(tag -> {
                PostTag postTag = new PostTag(post, new Tag("tempData")); // 0l: temp data

                Tag foundTag = tagRepository.findByTagName(tag.getTagName());
                if(foundTag == null) {
                    Tag save = tagRepository.save(tag);
                    postTag.setTag(save);
                }
                else {
                    postTag.setTag(foundTag);
                }
                postTagRepository.save(postTag);
            });
        }

    }
    public Set<Tag> json2TagSet(String json) {

        JSONArray jsonArray = new JSONArray(json);
        Set<Tag> outputs = new HashSet<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            String value = (String) jsonArray.getJSONObject(i).get("value");

            outputs.add(new Tag(value));
        }

        return outputs;
    }
    private List<SectionType> json2SectionTypeList(String json) {
        JSONArray jsonArray = new JSONArray(json);
        List<SectionType> outputs = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            String value = (String) jsonArray.getJSONObject(i).get("value");
            outputs.add(SectionType.valueOf(value));
        }

        // 앞 validation에서 검증 완료
        return outputs;
    }
}
