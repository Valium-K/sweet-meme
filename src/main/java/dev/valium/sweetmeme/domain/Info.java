package dev.valium.sweetmeme.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Period;

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

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String picImage;

    private String description;

    public static Info createInfo(String picImage, String head, String description) {
        return Info.builder()
                    .picImage(picImage)
                    .head(head)
                    .description(description)
                    .build();
    }

}
