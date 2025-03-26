package com.walab.nanuri.image.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.walab.nanuri.image.common.ImageExtension;
import com.walab.nanuri.image.dto.request.ImageRequestDto;
import com.walab.nanuri.item.entity.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false, length = 50)
    private String fileName;

    @Column(name = "file_path", nullable = false, unique = true, length = 500)
    private String filePath;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_type", nullable = false)
    private ImageExtension fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @CreatedDate
    @Column(name = "reg_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;

    public Image(ImageRequestDto.ImageCreateDto request, Item item) {
        this.fileName = request.getFileName();
        this.filePath = request.getFileName();
        this.fileType = request.getFileType();
        this.fileSize = request.getFileSize();
        this.item = item;
    }
}
