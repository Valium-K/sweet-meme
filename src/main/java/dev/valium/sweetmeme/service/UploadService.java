package dev.valium.sweetmeme.service;

import com.zakgof.webp4j.Webp4j;
import dev.valium.sweetmeme.config.FileConfig;
import dev.valium.sweetmeme.domain.*;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.repository.PostTagRepository;
import dev.valium.sweetmeme.repository.SectionRepository;
import dev.valium.sweetmeme.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {

    // TODO 윈도우에서 봐도 \가 아는 /로 보인다. DB상에서만 그런걸수도 있으니 나중에 확인할 것.
    private final String ABSOLUTE_UPLOAD_PATH = FileConfig.ABSOLUTE_UPLOAD_PATH;

    private final MemberRepository memberRepository;
    private final SectionRepository sectionRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;


    public void uploadPost(Member member, String title, String jsonTags, String jsonSectionType, MultipartFile file) throws Exception {

        // 파일생성
        File newFile = createNewFile(ABSOLUTE_UPLOAD_PATH, file, true);

        // post - sectionType 설정
        SectionType sectionType = json2SectionTypeList(jsonSectionType).get(0);
        Post post = Post.createPost(title, sectionType);

        // post - 파일 url 설정
        post.setPostImageUrl(newFile.getName());

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


    private File createNewFile(String path, MultipartFile file, boolean encode) throws IOException {

        String newFileName = UUID.randomUUID().toString().replace("-", "");
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
        File newFile;

        if(encode && !"mp4".equals(fileType)) {
            newFile = image2webp(file, 80f, path, newFileName);
        } else {
            newFile = new File(path, newFileName + "." + fileType);
        }

        if(!newFile.exists()){
            newFile.mkdirs();
        }

        return newFile;
    }
    private File image2webp(MultipartFile file, float quality, String path, String fileName) throws IOException {
        BufferedImage read = ImageIO.read(file.getInputStream());

        byte[] bytes = Webp4j.encode(read, quality);

        File newFile = new File(path, fileName + ".webp");
        FileOutputStream lFileOutputStream = new FileOutputStream(newFile);
        lFileOutputStream.write(bytes);
        lFileOutputStream.close();

        return newFile;
    }
    private void postTagSettingsProcess(Post post, String jsonTags) throws Exception {
        // post -> tag 태그 생성 밑 관계설정
        if(jsonTags != null && !"".equals(jsonTags)) {
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
        System.out.println("===========" + json);
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

        List<String> sectionNames = Arrays.stream(SectionType.values())
                                        .map(Enum::name)
                                        .collect(Collectors.toList());

        for(int i = 0; i < jsonArray.length(); i++) {
            String value = (String) jsonArray.getJSONObject(i).get("value");
            outputs.add(SectionType.valueOf(value));
        }

        return outputs;
    }

}
