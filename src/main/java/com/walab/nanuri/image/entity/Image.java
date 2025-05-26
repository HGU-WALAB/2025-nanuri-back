package com.walab.nanuri.image.entity;

import com.walab.nanuri.image.common.ImageExtension;
import com.walab.nanuri.item.entity.Item;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "file_url", nullable = false, unique = true, length = 500)
    private String fileUrl;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_type", nullable = false)
    private ImageExtension fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
}
