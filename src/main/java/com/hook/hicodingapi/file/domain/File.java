package com.hook.hicodingapi.file.domain;

import com.hook.hicodingapi.common.domain.BaseEntity;
import com.hook.hicodingapi.file.domain.type.AccessStatus;
import com.hook.hicodingapi.msg.domain.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.hook.hicodingapi.file.domain.type.AccessStatus.MESSAGE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_file")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long fileNo;

    @Column(nullable = false)
    private String fileName;

    @Column
    private String fileDescription;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private String size;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private AccessStatus accessStatus = MESSAGE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "msgNo")
    private Message message;



    @Column(nullable = false)
    private String fileUrl;

    public File(String fileName, String extension, String size, Message message, String fileUrl) {
        this.fileName = fileName;
        this.extension = extension;
        this.size = size;
        this.message = message;
        this.fileUrl = fileUrl;
    }


    public static File of(String fileName, String extension, String size, Message message, String fileUrl) {
        return new File(
                fileName,
                extension,
                size,
                message,
                fileUrl
        );
    }
}
