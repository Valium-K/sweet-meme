package dev.valium.sweetmeme.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import dev.valium.sweetmeme.domain.Member;
import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.domain.Section;
import dev.valium.sweetmeme.domain.Tag;
import dev.valium.sweetmeme.domain.enums.SectionType;
import dev.valium.sweetmeme.repository.MemberRepository;
import dev.valium.sweetmeme.repository.SectionRepository;
import dev.valium.sweetmeme.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {

    private final MemberRepository memberRepository;
    private final SectionRepository sectionRepository;
    private final TagRepository tagRepository;

    public void uploadPost(Member member, String title, String jsonTags, String jsonSectionType) throws JSONException {

        // TODO
        // post조립
        // sectionType얻기
        SectionType sectionType = json2SectionTypeList(jsonSectionType).get(0);

        // post 생성
        Post post = Post.createPost(title, sectionType);

        // post 조립 1
        Section section = sectionRepository.findBySectionType(sectionType);
        post.setSection(section);
        section.getPosts().add(post);

        // post 조립 2
        Set<Tag> tags = json2TagSet(jsonTags);
        tags.forEach(tagRepository::save);
        post.setTags(tags);

        Member foundMember = memberRepository.findMemberById(member.getId());
        post.setOriginalPoster(foundMember);
        foundMember.getMyPosts().add(post);
    }


    private Set<Tag> json2TagSet(String json) throws JSONException {

        JSONArray jsonArray = new JSONArray(json);
        Set<Tag> outputs = new HashSet<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            String value = (String) jsonArray.getJSONObject(i).get("value");

            // TODO 중복확인이 set차원에서 걸러질거같다. 확인은 해보기
            outputs.add(new Tag(value));
        }

        return outputs;
    }

    private List<SectionType> json2SectionTypeList(String json) throws JSONException {
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
