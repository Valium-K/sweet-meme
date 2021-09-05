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

    @Column(nullable = false)
    private String head;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String picImage;

    private String description;

    public void setHead(String head) {
        this.head = head;
    }
    public void setPicUrl(String picUrl) {
        this.picImage    = picUrl;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
