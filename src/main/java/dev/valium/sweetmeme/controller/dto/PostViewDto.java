package dev.valium.sweetmeme.controller.dto;

import dev.valium.sweetmeme.domain.Post;
import dev.valium.sweetmeme.domain.enums.SectionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostViewDto {

    private Long id;
    private String title;
    private String postImageUrl;
    private int upVote;
    private int downVote;
    private int commentCount;
    private SectionType belongedSectionType;
    private LocalDateTime createdDate;
    private String picImage;


    public PostViewDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.postImageUrl = post.getPostImageUrl();
        this.upVote = post.getVote().getUpVote();
        this.downVote = post.getVote().getDownVote();
        this.commentCount = post.getCommentCount();
        this.belongedSectionType = post.getBelongedSectionType();
        this.createdDate = post.getCreatedDate();
        this.picImage = post.getSection().getSectionInfo().getPicImage();
    }
}
