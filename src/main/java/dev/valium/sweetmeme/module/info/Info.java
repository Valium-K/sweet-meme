package dev.valium.sweetmeme.module.info;

import lombok.*;

import javax.persistence.*;

/**
 * Info entity는 member, section의 info에 공통으로 쓰임에 주의한다.
 * head는 맴버 이름의 cache거나 section의 이름으로 쓰인다.
 */
@Entity
@Getter @Setter
@Builder
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class Info {
    @Id @GeneratedValue
    @Column(name = "info_id")
    private Long id;

    private String head;

    private String picImage;

    private String description;

    private String stateCode;

    public static Info createInfo(String picImage, String head, String description) {
        return Info.builder()
                    .picImage(picImage)
                    .head(head)
                    .description(description)
                    .build();
    }

}
