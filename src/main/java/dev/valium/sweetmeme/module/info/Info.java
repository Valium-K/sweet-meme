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
