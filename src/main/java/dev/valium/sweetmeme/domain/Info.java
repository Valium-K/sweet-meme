package dev.valium.sweetmeme.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Info {
    @Id @GeneratedValue
    @Column(name = "info_id")
    private Long id;

    @Column(nullable = false)
    private String head;

    private String picUrl;
    private String description;

    public void setHead(String head) {
        this.head = head;
    }
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
