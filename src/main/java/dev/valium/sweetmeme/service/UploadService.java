package dev.valium.sweetmeme.service;

import dev.valium.sweetmeme.domain.*;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.repository.PostTagRepository;
import dev.valium.sweetmeme.repository.SectionRepository;
import dev.valium.sweetmeme.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {

    // TODO 윈도우에서 봐도 \가 아는 /로 보인다. DB상에서만 그런걸수도 있으니 나중에 확인할 것.
    private final String UPLOAD_PATH = "D:/sweetmeme/image/";

    private final MemberRepository memberRepository;
    private final SectionRepository sectionRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;


    public void uploadPost(Member member, String title, String jsonTags, String jsonSectionType, MultipartFile file) throws Exception {

        // 파일생성
        File newFile = createNewFile(UPLOAD_PATH, file);

        // post - sectionType 설정
        SectionType sectionType = json2SectionTypeList(jsonSectionType).get(0);
        Post post = Post.createPost(title, sectionType);

        // post - 파일 url 설정
        post.setPostImageUrl(newFile.getAbsolutePath());

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
        file.transferTo(newFile);
    }



    /**
     * fiel path가 UPLOAD_PATH + UUID가 되어 혼동 될 수 있음에도 file을 UUID로 지정 하지 않은 이유는
     * path를 참조 엔티티는 post뿐이며, 한 폴더에 크고 많은 파일이 생길경우 탐색기 사용시 느려질것을 우려해서이다.
     * @param path
     * @param file
     * @return
     * @throws Exception
     */
    private File createNewFile(String path, MultipartFile file) throws Exception {
        String upload_path = path + UUID.randomUUID().toString().replace("-", "");
        String fileName = file.getOriginalFilename().replace(" ", "");

        if("".equals(fileName)) {
            throw new Exception("fileName is null");
        }

        File newFile = new File(upload_path, fileName);

        if(!newFile.exists()){
            newFile.mkdirs();
        }

        return newFile;
    }
    private void postTagSettingsProcess(Post post, String jsonTags) throws Exception {
        // post -> tag 태그 생성 밑 관계설정
        if(!"".equals(jsonTags)) {
            Set<Tag> tags = json2TagSet(jsonTags);

            tags.forEach(tag -> {
                PostTag postTag = new PostTag(post, 0l); // 0l: temp data

                Tag foundTag = tagRepository.findByTagName(tag.getTagName());
                if(foundTag == null) {
                    Tag save = tagRepository.save(tag);
                    postTag.setTagId(save.getId());
                }
                else {
                    postTag.setTagId(foundTag.getId());
                }
                postTagRepository.save(postTag);
            });
        }

    }

    public Set<Tag> json2TagSet(String json) throws Exception {

        JSONArray jsonArray = new JSONArray(json);
        Set<Tag> outputs = new HashSet<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            String value = (String) jsonArray.getJSONObject(i).get("value");

            outputs.add(new Tag(value));
        }

        return outputs;
    }
    public List<SectionType> json2SectionTypeList(String json) throws Exception {
        JSONArray jsonArray = new JSONArray(json);
        List<SectionType> outputs = new ArrayList<>();

        List<String> sectionNames = Arrays.stream(SectionType.values())
                                        .map(Enum::name)
                                        .collect(Collectors.toList());

        for(int i = 0; i < jsonArray.length(); i++) {
            String value = (String) jsonArray.getJSONObject(i).get("value");

            if(sectionNames.contains(value))
                outputs.add(SectionType.valueOf(value));
            else {
                // TODO front hacking -> 404
                throw new IllegalArgumentException(value + "에 해당하는 section을 찾을 수 없습니다.");
            }
        }

        return outputs;
    }

}
