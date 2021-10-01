package dev.valium.sweetmeme.module.info;

import lombok.*;

import javax.persistence.*;

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

    // Member와 info를 조인시 nickname과 head의 값은 같지만,
    // post에서 info만 단독으로 쓰는 경우가 많아 추가.
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
