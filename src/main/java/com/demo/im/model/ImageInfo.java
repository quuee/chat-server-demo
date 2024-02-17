package com.demo.im.model;

import lombok.Data;

@Data
public class ImageInfo {

    private String imageUrl;
    private String imageLocalPath;
    private Double imageWidth;
    private Double imageHeight;
    private Double fileSize;
}
