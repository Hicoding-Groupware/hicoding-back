package com.hook.hicodingapi.common.domain;

import com.hook.hicodingapi.common.domain.type.StatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

import static com.hook.hicodingapi.common.domain.type.StatusType.USABLE;
import static javax.persistence.EnumType.STRING;

@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    protected LocalDateTime modifiedAt;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    protected StatusType status = USABLE;

}
