package dev.valium.sweetmeme.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class Info {
    @Id @GeneratedValue
    @Column(name = "info_id")
    private Long id;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String picImage;

    private String description;

    public void setPicUrl(String picUrl) {
        this.picImage    = picUrl;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
