package dev.valium.sweetmeme.module.bases;

import lombok.Getter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.ZonedDateTime;

/**
 * @PrePresist @PreUpdate를 정의한 추상 클래스의 상속을 이용해
 * EntityListeners를 대체해 사용 한다.
 */
@Getter
@MappedSuperclass
public abstract class BaseEntityZonedTime {

    private ZonedDateTime createdDate;
    private ZonedDateTime lastModifiedDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = ZonedDateTime.now();
        this.lastModifiedDate = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastModifiedDate = ZonedDateTime.now();
    }

}
