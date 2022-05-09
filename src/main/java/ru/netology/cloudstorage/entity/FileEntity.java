package ru.netology.cloudstorage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @JsonIgnore
    private long userId;
    @Column
    private String filename;
    @Column
    @JsonIgnore
    private String fileType;
    @Column
    private Long size;
    @JsonIgnore
    @Column(length = 100000000)
    @Lob
    private byte[] data;

    public FileEntity(String filename, long userId, String fileType,Long size, byte[] data) {
        this.filename = filename;
        this.userId = userId;
        this.fileType = fileType;
        this.size = size;
        this.data = data;
    }
}
